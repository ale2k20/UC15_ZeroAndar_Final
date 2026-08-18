/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 * UC15 - Projeto Integrador 3 - Etapa 8
 *
 * @author Alex
 * @since 15 de agosto de 2026
 * @version 1.8
 */
package uc15.etapa8.zeroandar.repository.jdbc;

import uc15.etapa8.zeroandar.repository.CorretorDAO;

import uc15.etapa8.zeroandar.model.PessoaCorretor;
import uc15.etapa8.zeroandar.model.Endereco;
import uc15.etapa8.zeroandar.infrastructure.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcCorretorDAO implements CorretorDAO {

    private Connection connection;
    private JdbcEnderecoDAO enderecoDAO;

    public JdbcCorretorDAO() {
        try {
            this.connection = DatabaseConnection.getInstance().getConnection();
            this.enderecoDAO = new JdbcEnderecoDAO();
        } catch (SQLException e) {
            System.err.println("Erro ao obter conexão: " + e.getMessage());
        }
    }

    /**
     * Insere um novo corretor
     */
    public int inserir(PessoaCorretor corretor) throws SQLException {
        connection.setAutoCommit(false);

        try {
            // 1. Insere endereco (se existir)
            int idEndereco = 0;
            if (corretor.getEndereco() != null) {
                idEndereco = enderecoDAO.inserir(corretor.getEndereco());
            }

            // 2. Insere Pessoa
            String sqlPessoa = "INSERT INTO Pessoa (nome, email, cpf, rg, tipo_pessoa, "
                    + "id_endereco, observacoes, ativo) VALUES (?, ?, ?, ?, 'F', ?, ?, ?)";

            int idPessoa = 0;
            try (PreparedStatement stmt = connection.prepareStatement(sqlPessoa, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, corretor.getNome());
                stmt.setString(2, corretor.getEmail());
                stmt.setString(3, corretor.getCpf());
                stmt.setString(4, corretor.getRg());
                stmt.setObject(5, idEndereco > 0 ? idEndereco : null);
                stmt.setString(6, corretor.getObservacoes());
                stmt.setBoolean(7, corretor.isAtivo());

                stmt.executeUpdate();

                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    idPessoa = rs.getInt(1);
                    corretor.setIdPessoa(idPessoa);
                }
            }

            // 3. Insere PessoaCorretor
            String sqlCorretor = "INSERT INTO PessoaCorretor (id_pessoa, creci, comissao_percentual, "
                    + "data_admissao, especialidade, meta_mensal) VALUES (?, ?, ?, ?, ?, ?)";

            int idCorretor = 0;
            try (PreparedStatement stmt = connection.prepareStatement(sqlCorretor, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, idPessoa);
                stmt.setString(2, corretor.getCreci());
                stmt.setDouble(3, corretor.getComissaoPercentual());
                stmt.setDate(4, new java.sql.Date(corretor.getDataAdmissao().getTime()));
                stmt.setString(5, corretor.getEspecialidade());
                stmt.setDouble(6, corretor.getMetaMensal());

                stmt.executeUpdate();

                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    idCorretor = rs.getInt(1);
                    corretor.setIdPessoaCorretor(idCorretor);
                }
            }

            connection.commit();
            return idCorretor;

        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    /**
     * Atualiza corretor existente
     */
    public boolean atualizar(PessoaCorretor corretor) throws SQLException {
        connection.setAutoCommit(false);

        try {
            // 1. Atualiza endereco
            if (corretor.getEndereco() != null && corretor.getEndereco().getIdEndereco() > 0) {
                enderecoDAO.atualizar(corretor.getEndereco());
            }

            // 2. Atualiza Pessoa
            String sqlPessoa = "UPDATE Pessoa SET nome = ?, email = ?, cpf = ?, rg = ?, "
                    + "observacoes = ?, ativo = ? WHERE id_pessoa = ?";

            try (PreparedStatement stmt = connection.prepareStatement(sqlPessoa)) {
                stmt.setString(1, corretor.getNome());
                stmt.setString(2, corretor.getEmail());
                stmt.setString(3, corretor.getCpf());
                stmt.setString(4, corretor.getRg());
                stmt.setString(5, corretor.getObservacoes());
                stmt.setBoolean(6, corretor.isAtivo());
                stmt.setInt(7, corretor.getIdPessoa());

                stmt.executeUpdate();
            }

            // 3. Atualiza PessoaCorretor
            String sqlCorretor = "UPDATE PessoaCorretor SET creci = ?, comissao_percentual = ?, "
                    + "especialidade = ?, meta_mensal = ? WHERE id_pessoa_corretor = ?";

            try (PreparedStatement stmt = connection.prepareStatement(sqlCorretor)) {
                stmt.setString(1, corretor.getCreci());
                stmt.setDouble(2, corretor.getComissaoPercentual());
                stmt.setString(3, corretor.getEspecialidade());
                stmt.setDouble(4, corretor.getMetaMensal());
                stmt.setInt(5, corretor.getIdPessoaCorretor());

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
     * Busca corretor por CRECI
     */
    public PessoaCorretor buscarPorCreci(String creci) throws SQLException {
        String sql = "SELECT p.*, pc.* FROM Pessoa p "
                + "INNER JOIN PessoaCorretor pc ON p.id_pessoa = pc.id_pessoa "
                + "WHERE pc.creci = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, creci);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extrairCorretor(rs);
            }
        }
        return null;
    }

    /**
     * Busca corretor por ID
     */
    public PessoaCorretor buscarPorId(int idCorretor) throws SQLException {
        String sql = "SELECT p.*, pc.* FROM Pessoa p "
                + "INNER JOIN PessoaCorretor pc ON p.id_pessoa = pc.id_pessoa "
                + "WHERE pc.id_pessoa_corretor = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idCorretor);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extrairCorretor(rs);
            }
        }
        return null;
    }

    /**
     * Lista todos os corretores (APENAS ATIVOS) - CORRIGIDO
     */
    public List<PessoaCorretor> listarTodos() throws SQLException {
        List<PessoaCorretor> lista = new ArrayList<>();
        // CORRECAO: Adiciona filtro WHERE p.ativo = true
        String sql = "SELECT p.*, pc.* FROM Pessoa p "
                + "INNER JOIN PessoaCorretor pc ON p.id_pessoa = pc.id_pessoa "
                + "WHERE p.ativo = true "
                + // ? CORRECAO AQUI!
                "ORDER BY p.nome";

        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(extrairCorretor(rs));
            }
        }
        return lista;
    }

    /**
     * Lista corretores ativos
     */
    public List<PessoaCorretor> listarAtivos() throws SQLException {
        List<PessoaCorretor> lista = new ArrayList<>();
        String sql = "SELECT p.*, pc.* FROM Pessoa p "
                + "INNER JOIN PessoaCorretor pc ON p.id_pessoa = pc.id_pessoa "
                + "WHERE p.ativo = true AND pc.data_desligamento IS NULL ORDER BY p.nome";

        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(extrairCorretor(rs));
            }
        }
        return lista;
    }

    /**
     * Busca corretores por especialidade
     */
    public List<PessoaCorretor> buscarPorEspecialidade(String especialidade) throws SQLException {
        List<PessoaCorretor> lista = new ArrayList<>();
        String sql = "SELECT p.*, pc.* FROM Pessoa p "
                + "INNER JOIN PessoaCorretor pc ON p.id_pessoa = pc.id_pessoa "
                + "WHERE p.ativo = true AND pc.especialidade LIKE ? ORDER BY p.nome"; // Adiciona filtro ativo

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + especialidade + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(extrairCorretor(rs));
            }
        }
        return lista;
    }

    /**
     * Exclui corretor (soft delete) - JA ESTAVA CORRETO
     */
    public boolean excluir(String creci) throws SQLException {
        PessoaCorretor corretor = buscarPorCreci(creci);
        if (corretor == null) {
            return false;
        }

        // Marca como inativo (soft delete)
        String sql = "UPDATE Pessoa SET ativo = false WHERE id_pessoa = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, corretor.getIdPessoa());
            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Conta total de corretores ATIVOS - CORRIGIDO
     */
    public int contarTotal() throws SQLException {
        String sql = "SELECT COUNT(*) FROM PessoaCorretor pc "
                + "INNER JOIN Pessoa p ON pc.id_pessoa = p.id_pessoa "
                + "WHERE p.ativo = true"; // ? CORREÇÃO AQUI!

        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    /**
     * Conta corretores online (simulacao - retorna ativos)
     */
    public int contarOnline() throws SQLException {
        return listarAtivos().size();
    }

    /**
     * Extrai PessoaCorretor do ResultSet
     */
    private PessoaCorretor extrairCorretor(ResultSet rs) throws SQLException {
        PessoaCorretor corretor = new PessoaCorretor();

        // Dados de Pessoa
        corretor.setIdPessoa(rs.getInt("id_pessoa"));
        corretor.setNome(rs.getString("nome"));
        corretor.setEmail(rs.getString("email"));
        corretor.setCpf(rs.getString("cpf"));
        corretor.setRg(rs.getString("rg"));
        corretor.setObservacoes(rs.getString("observacoes"));
        corretor.setAtivo(rs.getBoolean("ativo"));

        // Dados de PessoaCorretor
        corretor.setIdPessoaCorretor(rs.getInt("id_pessoa_corretor"));
        corretor.setCreci(rs.getString("creci"));
        corretor.setComissaoPercentual(rs.getDouble("comissao_percentual"));

        java.sql.Date dataAdm = rs.getDate("data_admissao");
        if (dataAdm != null) {
            corretor.setDataAdmissao(new java.util.Date(dataAdm.getTime()));
        }

        java.sql.Date dataDesl = rs.getDate("data_desligamento");
        if (dataDesl != null) {
            corretor.setDataDesligamento(new java.util.Date(dataDesl.getTime()));
        }

        corretor.setEspecialidade(rs.getString("especialidade"));
        corretor.setMetaMensal(rs.getDouble("meta_mensal"));

        // Carrega endereco
        int idEndereco = rs.getInt("id_endereco");
        if (idEndereco > 0 && !rs.wasNull()) {
            Endereco endereco = enderecoDAO.buscarPorId(idEndereco);
            corretor.setEndereco(endereco);
        }

        return corretor;
    }

    @Override
    public PessoaCorretor buscarPorCpf(String cpf) throws SQLException {
        String sql = "SELECT p.*, pc.* FROM Pessoa p "
                + "INNER JOIN PessoaCorretor pc ON p.id_pessoa = pc.id_pessoa "
                + "WHERE p.cpf = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, cpf);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return extrairCorretor(rs);
            }
        }
        return null;
    }
}
