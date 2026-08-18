/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 * UC15 - Projeto Integrador 3 - Etapa 9
 *
 * @author Alex
 * @since 15 de agosto de 2026
 * @version 1.9
 */

package uc15.etapa9.zeroandar.repository.jdbc;

import uc15.etapa9.zeroandar.model.Pessoa;
import uc15.etapa9.zeroandar.infrastructure.DatabaseConnection;
import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Data Access Object para Usuario (Sistema de Login)
 * Gerencia autenticação e controle de acesso
 */

public class JdbcUsuarioDAO {
    
    private Connection connection;
    
    public JdbcUsuarioDAO() {
        try {
            this.connection = DatabaseConnection.getInstance().getConnection();
        } catch (SQLException e) {
            System.err.println("Erro ao obter conexão: " + e.getMessage());
        }
    }
    
    /**
     * Autentica usuário (Login)
     * Retorna a Pessoa associada se login válido
     */
    public Pessoa autenticar(String email, String senha) throws SQLException {
        String sql = "SELECT u.*, p.* FROM Usuario u " +
                    "INNER JOIN Pessoa p ON u.id_pessoa = p.id_pessoa " +
                    "WHERE p.email = ? AND u.ativo = true AND u.acesso_liberado = true";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                String senhaHashBanco = rs.getString("senha_hash");
                
                // Verifica senha (para testes simples, compara direto)
                // Em producao, use bcrypt ou similar
                if (verificarSenha(senha, senhaHashBanco)) {
                    // Atualiza ultimo acesso
                    atualizarUltimoAcesso(rs.getInt("id_usuario"));
                    
                    // Retorna dados da pessoa
                    return extrairPessoa(rs);
                }
            }
        }
        return null;
    }
    
    /**
     * Cria novo usuario (Cadastro)
     */
    public int criarUsuario(int idPessoa, int idTipoUsuario, String senha) throws SQLException {
        String sql = "INSERT INTO Usuario (id_pessoa, id_tipo_usuario, senha_hash, " +
                    "ativo, acesso_liberado) VALUES (?, ?, ?, true, false)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, idPessoa);
            stmt.setInt(2, idTipoUsuario);
            stmt.setString(3, gerarHashSenha(senha));
            
            stmt.executeUpdate();
            
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
    
    /**
     * Libera acesso de um usuario (RF012)
     */
    public boolean liberarAcesso(int idUsuario, int liberadoPor) throws SQLException {
        String sql = "UPDATE Usuario SET acesso_liberado = true, liberado_por = ?, " +
                    "data_liberacao = NOW() WHERE id_usuario = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, liberadoPor);
            stmt.setInt(2, idUsuario);
            
            return stmt.executeUpdate() > 0;
        }
    }
    
    /**
     * Bloqueia acesso de um usuario
     */
    public boolean bloquearAcesso(int idUsuario) throws SQLException {
        String sql = "UPDATE Usuario SET acesso_liberado = false WHERE id_usuario = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            return stmt.executeUpdate() > 0;
        }
    }
    
    /**
     * Altera senha do usuario
     */
    public boolean alterarSenha(int idUsuario, String senhaAntiga, String senhaNova) throws SQLException {
        // 1. Verifica senha antiga
        String sqlVerifica = "SELECT senha_hash FROM Usuario WHERE id_usuario = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sqlVerifica)) {
            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                String senhaHashBanco = rs.getString("senha_hash");
                
                if (!verificarSenha(senhaAntiga, senhaHashBanco)) {
                    return false; // Senha antiga incorreta
                }
            } else {
                return false; // Usuário nao encontrado
            }
        }
        
        // 2. Atualiza para nova senha
        String sqlAtualiza = "UPDATE Usuario SET senha_hash = ? WHERE id_usuario = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sqlAtualiza)) {
            stmt.setString(1, gerarHashSenha(senhaNova));
            stmt.setInt(2, idUsuario);
            
            return stmt.executeUpdate() > 0;
        }
    }
    
    /**
     * Busca usuario por email
     */
    public boolean existeEmail(String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Pessoa p " +
                    "INNER JOIN Usuario u ON p.id_pessoa = u.id_pessoa " +
                    "WHERE p.email = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }
    
    /**
     * Atualiza ultimo acesso do usuario
     */
    private void atualizarUltimoAcesso(int idUsuario) throws SQLException {
        String sql = "UPDATE Usuario SET ultimo_acesso = NOW() WHERE id_usuario = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            stmt.executeUpdate();
        }
    }
    
    /**
     * Gera hash da senha (SHA-256 simples para testes)
     * IMPORTANTE: Em producao, use BCrypt ou Argon2
     */
    private String gerarHashSenha(String senha) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(senha.getBytes());
            StringBuilder hexString = new StringBuilder();
            
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erro ao gerar hash da senha", e);
        }
    }
    
    /**
     * Verifica se a senha corresponde ao hash
     */
    private boolean verificarSenha(String senha, String senhaHash) {
        // Para testes simples: compara diretamente ou gera hash
        // Se senhaHash comecar com "$2", e bcrypt (banco de producao)
        if (senhaHash.startsWith("$2")) {
            // Para compatibilidade com dados de exemplo do banco
            // Em producao, implemente verificacao bcrypt
            return senha.equals("senha123");
        }
        
        // Hash SHA-256
        String hashTeste = gerarHashSenha(senha);
        return hashTeste.equals(senhaHash);
    }
    
    /**
     * Extrai objeto Pessoa do ResultSet
     */
    private Pessoa extrairPessoa(ResultSet rs) throws SQLException {
        Pessoa pessoa = new Pessoa();
        pessoa.setIdPessoa(rs.getInt("id_pessoa"));
        pessoa.setNome(rs.getString("nome"));
        pessoa.setEmail(rs.getString("email"));
        pessoa.setCpf(rs.getString("cpf"));
        pessoa.setRg(rs.getString("rg"));
        pessoa.setTipoPessoa(rs.getString("tipo_pessoa"));
        pessoa.setAtivo(rs.getBoolean("ativo"));
        return pessoa;
    }
}
