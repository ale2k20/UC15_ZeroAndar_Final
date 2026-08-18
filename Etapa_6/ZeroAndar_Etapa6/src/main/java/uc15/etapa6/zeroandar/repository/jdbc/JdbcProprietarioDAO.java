/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 * UC15 - Projeto Integrador 3 - Etapa 6
 *
 * @author Alex
 * @since 14 de agosto de 2026
 * @version 1.6
 */
package uc15.etapa6.zeroandar.repository.jdbc;

import uc15.etapa6.zeroandar.repository.ProprietarioDAO;

import uc15.etapa6.zeroandar.model.PessoaProprietario;
import uc15.etapa6.zeroandar.model.Endereco;
import uc15.etapa6.zeroandar.infrastructure.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object para PessoaProprietario
 */
public class JdbcProprietarioDAO implements ProprietarioDAO {

    private Connection connection;
    private JdbcEnderecoDAO enderecoDAO;

    public JdbcProprietarioDAO() {
        try {
            this.connection = DatabaseConnection.getInstance().getConnection();
            this.enderecoDAO = new JdbcEnderecoDAO();
        } catch (SQLException e) {
            System.err.println("Erro ao obter conexão: " + e.getMessage());
        }
    }

    /**
     * Insere um novo proprietario
     */
    public int inserir(PessoaProprietario proprietario) throws SQLException {
        connection.setAutoCommit(false);

        try {
            // 1. Insere endereco (se existir)
            int idEndereco = 0;
            if (proprietario.getEndereco() != null) {
                idEndereco = enderecoDAO.inserir(proprietario.getEndereco());
            }

            // 2. Insere na tabela Pessoa
            String sqlPessoa = "INSERT INTO Pessoa (nome, email, cpf, rg, tipo_pessoa, "
                    + "id_endereco, observacoes, ativo) VALUES (?, ?, ?, ?, 'F', ?, ?, ?)";

            int idPessoa = 0;
            try (PreparedStatement stmt = connection.prepareStatement(sqlPessoa, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, proprietario.getNome());
                stmt.setString(2, proprietario.getEmail());
                stmt.setString(3, proprietario.getCpf());
                stmt.setString(4, proprietario.getRg());
                stmt.setObject(5, idEndereco > 0 ? idEndereco : null);
                stmt.setString(6, proprietario.getObservacoes());
                stmt.setBoolean(7, proprietario.isAtivo());

                stmt.executeUpdate();

                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    idPessoa = rs.getInt(1);
                    proprietario.setIdPessoa(idPessoa);
                }
            }

            // 3. Insere na tabela PessoaProprietario
            String sqlProp = "INSERT INTO PessoaProprietario (id_pessoa, data_cadastro, aceita_contato) "
                    + "VALUES (?, ?, ?)";

            int idProprietario = 0;
            try (PreparedStatement stmt = connection.prepareStatement(sqlProp, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, idPessoa);
                stmt.setDate(2, new java.sql.Date(proprietario.getDataCadastro().getTime()));
                stmt.setBoolean(3, proprietario.isAceitaContato());

                stmt.executeUpdate();

                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    idProprietario = rs.getInt(1);
                    proprietario.setIdPessoaProprietario(idProprietario);
                }
            }

            connection.commit();
            return idProprietario;

        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    /**
     * Atualiza proprietario existente
     */
    public boolean atualizar(PessoaProprietario proprietario) throws SQLException {
        connection.setAutoCommit(false);

        try {
            // 1. Atualiza endereco
            if (proprietario.getEndereco() != null && proprietario.getEndereco().getIdEndereco() > 0) {
                enderecoDAO.atualizar(proprietario.getEndereco());
            }

            // 2. Atualiza Pessoa
            String sqlPessoa = "UPDATE Pessoa SET nome = ?, email = ?, cpf = ?, rg = ?, "
                    + "observacoes = ?, ativo = ? WHERE id_pessoa = ?";

            try (PreparedStatement stmt = connection.prepareStatement(sqlPessoa)) {
                stmt.setString(1, proprietario.getNome());
                stmt.setString(2, proprietario.getEmail());
                stmt.setString(3, proprietario.getCpf());
                stmt.setString(4, proprietario.getRg());
                stmt.setString(5, proprietario.getObservacoes());
                stmt.setBoolean(6, proprietario.isAtivo());
                stmt.setInt(7, proprietario.getIdPessoa());

                stmt.executeUpdate();
            }

            // 3. Atualiza PessoaProprietario
            String sqlProp = "UPDATE PessoaProprietario SET aceita_contato = ? "
                    + "WHERE id_pessoa_proprietario = ?";

            try (PreparedStatement stmt = connection.prepareStatement(sqlProp)) {
                stmt.setBoolean(1, proprietario.isAceitaContato());
                stmt.setInt(2, proprietario.getIdPessoaProprietario());

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
     * Busca proprietario por ID
     */
    public PessoaProprietario buscarPorId(int idProprietario) throws SQLException {
        String sql = "SELECT p.*, pp.* FROM Pessoa p "
                + "INNER JOIN PessoaProprietario pp ON p.id_pessoa = pp.id_pessoa "
                + "WHERE pp.id_pessoa_proprietario = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idProprietario);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extrairProprietario(rs);
            }
        }
        return null;
    }

    /**
     * Lista todos os proprietarios
     */
    public List<PessoaProprietario> listarTodos() throws SQLException {
        List<PessoaProprietario> lista = new ArrayList<>();
        String sql = "SELECT p.*, pp.* FROM Pessoa p "
                + "INNER JOIN PessoaProprietario pp ON p.id_pessoa = pp.id_pessoa "
                + "ORDER BY p.nome";

        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(extrairProprietario(rs));
            }
        }
        return lista;
    }

    /**
     * Lista proprietarios que aceitam contato
     */
    public List<PessoaProprietario> listarQueAceitamContato() throws SQLException {
        List<PessoaProprietario> lista = new ArrayList<>();
        String sql = "SELECT p.*, pp.* FROM Pessoa p "
                + "INNER JOIN PessoaProprietario pp ON p.id_pessoa = pp.id_pessoa "
                + "WHERE pp.aceita_contato = true ORDER BY p.nome";

        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(extrairProprietario(rs));
            }
        }
        return lista;
    }

    /**
     * Exclui proprietario (soft delete)
     */
    public boolean excluir(int idProprietario) throws SQLException {
        String sqlBusca = "SELECT id_pessoa FROM PessoaProprietario WHERE id_pessoa_proprietario = ?";
        int idPessoa = 0;

        try (PreparedStatement stmt = connection.prepareStatement(sqlBusca)) {
            stmt.setInt(1, idProprietario);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                idPessoa = rs.getInt("id_pessoa");
            }
        }

        if (idPessoa == 0) {
            return false;
        }

        String sql = "UPDATE Pessoa SET ativo = false WHERE id_pessoa = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idPessoa);
            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Conta total de proprietarios
     */
    public int contarTotal() throws SQLException {
        String sql = "SELECT COUNT(*) FROM PessoaProprietario";

        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    /**
     * Extrai PessoaProprietario do ResultSet
     */
    private PessoaProprietario extrairProprietario(ResultSet rs) throws SQLException {
        PessoaProprietario proprietario = new PessoaProprietario();

        // Dados de Pessoa
        proprietario.setIdPessoa(rs.getInt("id_pessoa"));
        proprietario.setNome(rs.getString("nome"));
        proprietario.setEmail(rs.getString("email"));
        proprietario.setCpf(rs.getString("cpf"));
        proprietario.setRg(rs.getString("rg"));
        proprietario.setObservacoes(rs.getString("observacoes"));
        proprietario.setAtivo(rs.getBoolean("ativo"));

        // Dados de PessoaProprietario
        proprietario.setIdPessoaProprietario(rs.getInt("id_pessoa_proprietario"));

        java.sql.Date dataCad = rs.getDate("data_cadastro");
        if (dataCad != null) {
            proprietario.setDataCadastro(new java.util.Date(dataCad.getTime()));
        }

        proprietario.setAceitaContato(rs.getBoolean("aceita_contato"));

        // Carrega endereco
        int idEndereco = rs.getInt("id_endereco");
        if (idEndereco > 0 && !rs.wasNull()) {
            Endereco endereco = enderecoDAO.buscarPorId(idEndereco);
            proprietario.setEndereco(endereco);
        }

        return proprietario;
    }

    @Override
    public List<PessoaProprietario> listarAtivos() throws SQLException {
        List<PessoaProprietario> lista = new ArrayList<>();
        String sql = "SELECT p.*, pp.* FROM Pessoa p "
                + "INNER JOIN PessoaProprietario pp ON p.id_pessoa = pp.id_pessoa "
                + "WHERE p.ativo = true "
                + "ORDER BY p.nome";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(extrairProprietario(rs));
            }
        }
        return lista;
    }

    @Override
    public PessoaProprietario buscarPorCpf(String cpf) throws SQLException {
        String sql = "SELECT p.*, pp.* FROM Pessoa p "
                + "INNER JOIN PessoaProprietario pp ON p.id_pessoa = pp.id_pessoa "
                + "WHERE p.cpf = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, cpf);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return extrairProprietario(rs);
            }
        }
        return null;
    }
}
