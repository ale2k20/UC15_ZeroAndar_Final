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

import uc15.etapa9.zeroandar.repository.ClienteDAO;

import uc15.etapa9.zeroandar.model.PessoaCliente;
import uc15.etapa9.zeroandar.model.Endereco;
import uc15.etapa9.zeroandar.infrastructure.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object para PessoaCliente
 * Gerencia persistencia de clientes no banco de dados
 */
public class JdbcClienteDAO implements ClienteDAO {

    private Connection connection;
    private JdbcEnderecoDAO enderecoDAO;

    public JdbcClienteDAO() {
        try {
            this.connection = DatabaseConnection.getInstance().getConnection();
            this.enderecoDAO = new JdbcEnderecoDAO();
        } catch (SQLException e) {
            System.err.println("Erro ao obter conexão: " + e.getMessage());
        }
    }

    /**
     * Insere um novo cliente no banco
     * Processo: 1) Insere Pessoa, 2) Insere PessoaCliente
     */
    public int inserir(PessoaCliente cliente) throws SQLException {
        connection.setAutoCommit(false);

        try {
            // 1. Insere endereço (se existir)
            int idEndereco = 0;
            if (cliente.getEndereco() != null) {
                idEndereco = enderecoDAO.inserir(cliente.getEndereco());
            }

            // 2. Insere na tabela Pessoa
            String sqlPessoa = "INSERT INTO Pessoa (nome, email, cpf, rg, tipo_pessoa, " +
                    "data_nascimento, id_endereco, observacoes, ativo) " +
                    "VALUES (?, ?, ?, ?, 'F', ?, ?, ?, ?)";

            int idPessoa = 0;
            try (PreparedStatement stmt = connection.prepareStatement(sqlPessoa, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, cliente.getNome());
                stmt.setString(2, cliente.getEmail());
                stmt.setString(3, cliente.getCpf());
                stmt.setString(4, cliente.getRg());
                stmt.setDate(5,
                        cliente.getDataNascimento() != null ? new java.sql.Date(cliente.getDataNascimento().getTime())
                                : null);
                stmt.setObject(6, idEndereco > 0 ? idEndereco : null);
                stmt.setString(7, cliente.getObservacoes());
                stmt.setBoolean(8, cliente.isAtivo());

                stmt.executeUpdate();

                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    idPessoa = rs.getInt(1);
                    cliente.setIdPessoa(idPessoa);
                }
            }

            // Telefone principal: o front-end da Etapa 9 já possui esse campo.
            // Agora ele também é persistido na tabela TelefonePessoa.
            if (cliente.getTelefone() != null && !cliente.getTelefone().isBlank()) {
                String sqlTelefone = "INSERT INTO TelefonePessoa (id_pessoa, numero, tipo, principal, whatsapp) VALUES (?, ?, 'celular', true, false)";
                try (PreparedStatement stmt = connection.prepareStatement(sqlTelefone)) {
                    stmt.setInt(1, cliente.getIdPessoa());
                    stmt.setString(2, cliente.getTelefone());
                    stmt.executeUpdate();
                }
            }

            // 3. Insere na tabela PessoaCliente
            String sqlCliente = "INSERT INTO PessoaCliente (id_pessoa, data_cadastro, " +
                    "origem_cadastro, score_interesse) VALUES (?, ?, ?, ?)";

            int idPessoaCliente = 0;
            try (PreparedStatement stmt = connection.prepareStatement(sqlCliente, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, idPessoa);
                stmt.setDate(2, new java.sql.Date(cliente.getDataCadastro().getTime()));
                stmt.setString(3, cliente.getOrigemCadastro());
                stmt.setInt(4, cliente.getScoreInteresse());

                stmt.executeUpdate();

                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    idPessoaCliente = rs.getInt(1);
                    cliente.setIdPessoaCliente(idPessoaCliente);
                }
            }

            connection.commit();
            return idPessoaCliente;

        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    /**
     * Atualiza dados de um cliente existente
     */
    public boolean atualizar(PessoaCliente cliente) throws SQLException {
        connection.setAutoCommit(false);

        try {
            // 1. Atualiza endereco (se existir)
            if (cliente.getEndereco() != null && cliente.getEndereco().getIdEndereco() > 0) {
                enderecoDAO.atualizar(cliente.getEndereco());
            }

            // 2. Atualiza tabela Pessoa
            String sqlPessoa = "UPDATE Pessoa SET nome = ?, email = ?, cpf = ?, rg = ?, " +
                    "observacoes = ?, ativo = ? WHERE id_pessoa = ?";

            try (PreparedStatement stmt = connection.prepareStatement(sqlPessoa)) {
                stmt.setString(1, cliente.getNome());
                stmt.setString(2, cliente.getEmail());
                stmt.setString(3, cliente.getCpf());
                stmt.setString(4, cliente.getRg());
                stmt.setString(5, cliente.getObservacoes());
                stmt.setBoolean(6, cliente.isAtivo());
                stmt.setInt(7, cliente.getIdPessoa());

                stmt.executeUpdate();
            }

            // Atualiza o telefone principal.
            if (cliente.getTelefone() != null && !cliente.getTelefone().isBlank()) {
                String sqlTelefone = "DELETE FROM TelefonePessoa WHERE id_pessoa = ? AND principal = true";
                try (PreparedStatement stmt = connection.prepareStatement(sqlTelefone)) {
                    stmt.setInt(1, cliente.getIdPessoa());
                    stmt.executeUpdate();
                }
                sqlTelefone = "INSERT INTO TelefonePessoa (id_pessoa, numero, tipo, principal, whatsapp) VALUES (?, ?, 'celular', true, false)";
                try (PreparedStatement stmt = connection.prepareStatement(sqlTelefone)) {
                    stmt.setInt(1, cliente.getIdPessoa());
                    stmt.setString(2, cliente.getTelefone());
                    stmt.executeUpdate();
                }
            }

            // 3. Atualiza tabela PessoaCliente
            String sqlCliente = "UPDATE PessoaCliente SET origem_cadastro = ?, " +
                    "score_interesse = ? WHERE id_pessoa_cliente = ?";

            try (PreparedStatement stmt = connection.prepareStatement(sqlCliente)) {
                stmt.setString(1, cliente.getOrigemCadastro());
                stmt.setInt(2, cliente.getScoreInteresse());
                stmt.setInt(3, cliente.getIdPessoaCliente());

                stmt.executeUpdate();
            }

            connection.commit();
            return true;

        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    /**
     * Busca cliente por ID
     */
    public PessoaCliente buscarPorId(int idCliente) throws SQLException {
        String sql = "SELECT p.*, pc.* FROM Pessoa p " +
                "INNER JOIN PessoaCliente pc ON p.id_pessoa = pc.id_pessoa " +
                "WHERE pc.id_pessoa_cliente = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idCliente);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extrairCliente(rs);
            }
        }
        return null;
    }

    /**
     * Busca cliente por CPF
     */
    public PessoaCliente buscarPorCpf(String cpf) throws SQLException {
        String sql = "SELECT p.*, pc.* FROM Pessoa p " +
                "INNER JOIN PessoaCliente pc ON p.id_pessoa = pc.id_pessoa " +
                "WHERE p.cpf = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extrairCliente(rs);
            }
        }
        return null;
    }

    /**
     * Lista todos os clientes
     */
    public List<PessoaCliente> listarTodos() throws SQLException {
        List<PessoaCliente> lista = new ArrayList<>();
        String sql = "SELECT p.*, pc.* FROM Pessoa p " +
                "INNER JOIN PessoaCliente pc ON p.id_pessoa = pc.id_pessoa " +
                "ORDER BY p.nome";

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(extrairCliente(rs));
            }
        }
        return lista;
    }

    /**
     * Lista clientes ativos
     */
    public List<PessoaCliente> listarAtivos() throws SQLException {
        List<PessoaCliente> lista = new ArrayList<>();
        String sql = "SELECT p.*, pc.* FROM Pessoa p " +
                "INNER JOIN PessoaCliente pc ON p.id_pessoa = pc.id_pessoa " +
                "WHERE p.ativo = true ORDER BY p.nome";

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(extrairCliente(rs));
            }
        }
        return lista;
    }

    /**
     * Busca clientes por nome (LIKE)
     */
    public List<PessoaCliente> buscarPorNome(String nome) throws SQLException {
        List<PessoaCliente> lista = new ArrayList<>();
        String sql = "SELECT p.*, pc.* FROM Pessoa p " +
                "INNER JOIN PessoaCliente pc ON p.id_pessoa = pc.id_pessoa " +
                "WHERE p.nome LIKE ? ORDER BY p.nome";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + nome + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(extrairCliente(rs));
            }
        }
        return lista;
    }

    /**
     * Exclui um cliente (soft delete - marca como inativo)
     */
    public boolean excluir(int idCliente) throws SQLException {
        // Busca id_pessoa do cliente
        String sqlBusca = "SELECT id_pessoa FROM PessoaCliente WHERE id_pessoa_cliente = ?";
        int idPessoa = 0;

        try (PreparedStatement stmt = connection.prepareStatement(sqlBusca)) {
            stmt.setInt(1, idCliente);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                idPessoa = rs.getInt("id_pessoa");
            }
        }

        if (idPessoa == 0) {
            return false;
        }

        // Marca como inativo (soft delete)
        String sql = "UPDATE Pessoa SET ativo = false WHERE id_pessoa = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idPessoa);
            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Exclui permanentemente um cliente
     */
    public boolean excluirPermanente(int idCliente) throws SQLException {
        connection.setAutoCommit(false);

        try {
            // 1. Busca id_pessoa
            String sqlBusca = "SELECT id_pessoa FROM PessoaCliente WHERE id_pessoa_cliente = ?";
            int idPessoa = 0;

            try (PreparedStatement stmt = connection.prepareStatement(sqlBusca)) {
                stmt.setInt(1, idCliente);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    idPessoa = rs.getInt("id_pessoa");
                }
            }

            if (idPessoa == 0) {
                connection.rollback();
                return false;
            }

            // 2. Exclui PessoaCliente
            String sqlCliente = "DELETE FROM PessoaCliente WHERE id_pessoa_cliente = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sqlCliente)) {
                stmt.setInt(1, idCliente);
                stmt.executeUpdate();
            }

            // 3. Exclui Pessoa (CASCADE exclui endereco se configurado)
            String sqlPessoa = "DELETE FROM Pessoa WHERE id_pessoa = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sqlPessoa)) {
                stmt.setInt(1, idPessoa);
                stmt.executeUpdate();
            }

            connection.commit();
            return true;

        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    /**
     * Conta total de clientes
     */
    public int contarTotal() throws SQLException {
        String sql = "SELECT COUNT(*) FROM PessoaCliente";

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    /**
     * Conta clientes ativos
     */
    public int contarAtivos() throws SQLException {
        String sql = "SELECT COUNT(*) FROM Pessoa p " +
                "INNER JOIN PessoaCliente pc ON p.id_pessoa = pc.id_pessoa " +
                "WHERE p.ativo = true";

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    /**
     * Extrai objeto PessoaCliente do ResultSet
     */
    private PessoaCliente extrairCliente(ResultSet rs) throws SQLException {
        PessoaCliente cliente = new PessoaCliente();

        // Dados de Pessoa
        cliente.setIdPessoa(rs.getInt("id_pessoa"));
        cliente.setNome(rs.getString("nome"));
        cliente.setEmail(rs.getString("email"));
        cliente.setTelefone(buscarTelefonePrincipal(rs.getInt("id_pessoa")));
        cliente.setCpf(rs.getString("cpf"));
        cliente.setRg(rs.getString("rg"));
        cliente.setTipoPessoa(rs.getString("tipo_pessoa"));

        java.sql.Date dataNasc = rs.getDate("data_nascimento");
        if (dataNasc != null) {
            cliente.setDataNascimento(new java.util.Date(dataNasc.getTime()));
        }

        cliente.setObservacoes(rs.getString("observacoes"));
        cliente.setAtivo(rs.getBoolean("ativo"));

        // Dados de PessoaCliente
        cliente.setIdPessoaCliente(rs.getInt("id_pessoa_cliente"));

        java.sql.Date dataCad = rs.getDate("data_cadastro");
        if (dataCad != null) {
            cliente.setDataCadastro(new java.util.Date(dataCad.getTime()));
        }

        cliente.setOrigemCadastro(rs.getString("origem_cadastro"));
        cliente.setScoreInteresse(rs.getInt("score_interesse"));

        // Carrega endereco se existir
        int idEndereco = rs.getInt("id_endereco");
        if (idEndereco > 0 && !rs.wasNull()) {
            Endereco endereco = enderecoDAO.buscarPorId(idEndereco);
            cliente.setEndereco(endereco);
        }

        return cliente;
    }
    private String buscarTelefonePrincipal(int idPessoa) throws SQLException {
        String sql = "SELECT numero FROM TelefonePessoa WHERE id_pessoa = ? AND principal = true ORDER BY id_telefone LIMIT 1";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idPessoa);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getString("numero") : null;
            }
        }
    }

}
