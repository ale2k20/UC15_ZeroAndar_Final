/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 * UC15 - Projeto Integrador 3 - Etapa 7
 *
 * @author Alexandre
 * @since 14 de agosto de 2026
 * @version 1.7
 */
package uc15.etapa7.zeroandar.repository.jdbc;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import uc15.etapa7.zeroandar.infrastructure.DatabaseConnection;
import uc15.etapa7.zeroandar.model.Endereco;
import uc15.etapa7.zeroandar.model.Imovel;
import uc15.etapa7.zeroandar.model.PessoaCorretor;
import uc15.etapa7.zeroandar.model.PessoaProprietario;
import uc15.etapa7.zeroandar.repository.ImovelDAO;

/**
 * Data Access Object para Imovel
 */
public class JdbcImovelDAO implements ImovelDAO {

    private Connection connection;
    private JdbcEnderecoDAO enderecoDAO;
    private JdbcProprietarioDAO proprietarioDAO;
    private JdbcCorretorDAO corretorDAO;

    public JdbcImovelDAO() {
        try {
            this.connection = DatabaseConnection.getInstance().getConnection();
            this.enderecoDAO = new JdbcEnderecoDAO();
            this.proprietarioDAO = new JdbcProprietarioDAO();
            this.corretorDAO = new JdbcCorretorDAO();
        } catch (SQLException e) {
            System.err.println("Erro ao obter conexão: " + e.getMessage());
        }
    }

    /**
     * Insere um novo imovel
     */
    public int inserir(Imovel imovel) throws SQLException {
        connection.setAutoCommit(false);

        try {
            // 1. Insere endereco
            int idEndereco = 0;
            if (imovel.getEndereco() != null) {
                idEndereco = enderecoDAO.inserir(imovel.getEndereco());
            }

            // 2. Insere imovel
            String sql = "INSERT INTO Imovel (codigo_imovel, descricao, tipo, finalidade, "
                    + "valor_venda, valor_aluguel, valor_condominio, valor_iptu, area_total, "
                    + "area_construida, quartos, suites, banheiros, vagas_garagem, andar, "
                    + "aceita_permuta, aceita_financiamento, mobiliado, status, destaque, "
                    + "id_proprietario, id_corretor_responsavel, id_endereco) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            int idImovel = 0;
            try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, imovel.getCodigoImovel());
                stmt.setString(2, imovel.getDescricao());
                stmt.setString(3, imovel.getTipo());
                stmt.setString(4, imovel.getFinalidade());
                stmt.setDouble(5, imovel.getValorVenda());
                stmt.setDouble(6, imovel.getValorAluguel());
                stmt.setDouble(7, imovel.getValorCondominio());
                stmt.setDouble(8, imovel.getValorIptu());
                stmt.setDouble(9, imovel.getAreaTotal());
                stmt.setDouble(10, imovel.getAreaConstruida());
                stmt.setInt(11, imovel.getQuartos());
                stmt.setInt(12, imovel.getSuites());
                stmt.setInt(13, imovel.getBanheiros());
                stmt.setInt(14, imovel.getVagasGaragem());
                stmt.setInt(15, imovel.getAndar());
                stmt.setBoolean(16, imovel.isAceitaPermuta());
                stmt.setBoolean(17, imovel.isAceitaFinanciamento());
                stmt.setBoolean(18, imovel.isMobiliado());
                stmt.setString(19, imovel.getStatus());
                stmt.setBoolean(20, imovel.isDestaque());

                // IDs de proprietario e corretor
                if (imovel.getProprietario() != null) {
                    stmt.setInt(21, imovel.getProprietario().getIdPessoaProprietario());
                } else {
                    stmt.setNull(21, Types.INTEGER);
                }

                if (imovel.getCorretorResponsavel() != null) {
                    stmt.setInt(22, imovel.getCorretorResponsavel().getIdPessoaCorretor());
                } else {
                    stmt.setNull(22, Types.INTEGER);
                }

                stmt.setInt(23, idEndereco);

                stmt.executeUpdate();

                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    idImovel = rs.getInt(1);
                    imovel.setIdImovel(idImovel);
                }
            }

            connection.commit();
            return idImovel;

        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    /**
     * Atualiza imovel existente
     */
    public boolean atualizar(Imovel imovel) throws SQLException {
        connection.setAutoCommit(false);

        try {
            // 1. Atualiza endereco
            if (imovel.getEndereco() != null && imovel.getEndereco().getIdEndereco() > 0) {
                enderecoDAO.atualizar(imovel.getEndereco());
            }

            // 2. Atualiza Imovel
            String sql = "UPDATE Imovel SET codigo_imovel = ?, descricao = ?, tipo = ?, "
                    + "finalidade = ?, valor_venda = ?, valor_aluguel = ?, valor_condominio = ?, "
                    + "valor_iptu = ?, area_total = ?, area_construida = ?, quartos = ?, "
                    + "suites = ?, banheiros = ?, vagas_garagem = ?, andar = ?, aceita_permuta = ?, "
                    + "aceita_financiamento = ?, mobiliado = ?, status = ?, destaque = ?, "
                    + "id_proprietario = ?, id_corretor_responsavel = ? WHERE id_imovel = ?";

            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, imovel.getCodigoImovel());
                stmt.setString(2, imovel.getDescricao());
                stmt.setString(3, imovel.getTipo());
                stmt.setString(4, imovel.getFinalidade());
                stmt.setDouble(5, imovel.getValorVenda());
                stmt.setDouble(6, imovel.getValorAluguel());
                stmt.setDouble(7, imovel.getValorCondominio());
                stmt.setDouble(8, imovel.getValorIptu());
                stmt.setDouble(9, imovel.getAreaTotal());
                stmt.setDouble(10, imovel.getAreaConstruida());
                stmt.setInt(11, imovel.getQuartos());
                stmt.setInt(12, imovel.getSuites());
                stmt.setInt(13, imovel.getBanheiros());
                stmt.setInt(14, imovel.getVagasGaragem());
                stmt.setInt(15, imovel.getAndar());
                stmt.setBoolean(16, imovel.isAceitaPermuta());
                stmt.setBoolean(17, imovel.isAceitaFinanciamento());
                stmt.setBoolean(18, imovel.isMobiliado());
                stmt.setString(19, imovel.getStatus());
                stmt.setBoolean(20, imovel.isDestaque());

                if (imovel.getProprietario() != null) {
                    stmt.setInt(21, imovel.getProprietario().getIdPessoaProprietario());
                } else {
                    stmt.setNull(21, Types.INTEGER);
                }

                if (imovel.getCorretorResponsavel() != null) {
                    stmt.setInt(22, imovel.getCorretorResponsavel().getIdPessoaCorretor());
                } else {
                    stmt.setNull(22, Types.INTEGER);
                }

                stmt.setInt(23, imovel.getIdImovel());

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
     * Busca imovel por codigo
     */
    public Imovel buscarPorCodigo(String codigoImovel) throws SQLException {
        String sql = "SELECT * FROM Imovel WHERE codigo_imovel = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, codigoImovel);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extrairImovel(rs);
            }
        }
        return null;
    }

    /**
     * Busca imovel por ID
     */
    public Imovel buscarPorId(int idImovel) throws SQLException {
        String sql = "SELECT * FROM Imovel WHERE id_imovel = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idImovel);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extrairImovel(rs);
            }
        }
        return null;
    }

    /**
     * Lista todos os imoveis
     */
    public List<Imovel> listarTodos() throws SQLException {
        List<Imovel> lista = new ArrayList<>();
        String sql = "SELECT * FROM Imovel ORDER BY codigo_imovel";

        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(extrairImovel(rs));
            }
        }
        return lista;
    }

    /**
     * Lista imoveis disponiveis
     */
    public List<Imovel> listarDisponiveis() throws SQLException {
        return buscarPorStatus("disponivel");
    }

    /**
     * Lista imoveis em destaque
     */
    public List<Imovel> listarDestaques() throws SQLException {
        List<Imovel> lista = new ArrayList<>();
        String sql = "SELECT * FROM Imovel WHERE destaque = true AND status = 'disponivel' "
                + "ORDER BY data_criacao DESC";

        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(extrairImovel(rs));
            }
        }
        return lista;
    }

    /**
     * Busca imoveis por tipo
     */
    public List<Imovel> buscarPorTipo(String tipo) throws SQLException {
        List<Imovel> lista = new ArrayList<>();
        String sql = "SELECT * FROM Imovel WHERE tipo = ? ORDER BY codigo_imovel";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, tipo);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(extrairImovel(rs));
            }
        }
        return lista;
    }

    /**
     * Busca imoveis por finalidade
     */
    public List<Imovel> buscarPorFinalidade(String finalidade) throws SQLException {
        List<Imovel> lista = new ArrayList<>();
        String sql = "SELECT * FROM Imovel WHERE finalidade = ? OR finalidade = 'ambos' "
                + "ORDER BY codigo_imovel";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, finalidade);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(extrairImovel(rs));
            }
        }
        return lista;
    }

    /**
     * Busca imoveis por status
     */
    public List<Imovel> buscarPorStatus(String status) throws SQLException {
        List<Imovel> lista = new ArrayList<>();
        String sql = "SELECT * FROM Imovel WHERE status = ? ORDER BY codigo_imovel";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(extrairImovel(rs));
            }
        }
        return lista;
    }

    /**
     * Busca imoveis com filtros (RF010)
     */
    public List<Imovel> buscarComFiltros(String tipo, String finalidade, double valorMin,
            double valorMax, int quartosMin) throws SQLException {
        List<Imovel> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM Imovel WHERE 1=1");

        if (tipo != null && !tipo.isEmpty()) {
            sql.append(" AND tipo = ?");
        }
        if (finalidade != null && !finalidade.isEmpty()) {
            sql.append(" AND (finalidade = ? OR finalidade = 'ambos')");
        }
        if (valorMin > 0) {
            sql.append(" AND (valor_venda >= ? OR valor_aluguel >= ?)");
        }
        if (valorMax > 0) {
            sql.append(" AND (valor_venda <= ? OR valor_aluguel <= ?)");
        }
        if (quartosMin > 0) {
            sql.append(" AND quartos >= ?");
        }

        sql.append(" ORDER BY codigo_imovel");

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;

            if (tipo != null && !tipo.isEmpty()) {
                stmt.setString(paramIndex++, tipo);
            }
            if (finalidade != null && !finalidade.isEmpty()) {
                stmt.setString(paramIndex++, finalidade);
            }
            if (valorMin > 0) {
                stmt.setDouble(paramIndex++, valorMin);
                stmt.setDouble(paramIndex++, valorMin);
            }
            if (valorMax > 0) {
                stmt.setDouble(paramIndex++, valorMax);
                stmt.setDouble(paramIndex++, valorMax);
            }
            if (quartosMin > 0) {
                stmt.setInt(paramIndex++, quartosMin);
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(extrairImovel(rs));
            }
        }
        return lista;
    }

    /**
     * Exclui um imovel
     */
    public boolean excluir(String codigoImovel) throws SQLException {
        String sql = "DELETE FROM Imovel WHERE codigo_imovel = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, codigoImovel);
            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Conta total de imoveis
     */
    public int contarTotal() throws SQLException {
        String sql = "SELECT COUNT(*) FROM Imovel";

        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    /**
     * Conta imoveis por status
     */
    public long contarPorStatus(String status) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Imovel WHERE status = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getLong(1);
            }
        }
        return 0;
    }

    /**
     * Extrai objeto Imovel do ResultSet
     */
    private Imovel extrairImovel(ResultSet rs) throws SQLException {
        Imovel imovel = new Imovel();

        imovel.setIdImovel(rs.getInt("id_imovel"));
        imovel.setCodigoImovel(rs.getString("codigo_imovel"));
        imovel.setDescricao(rs.getString("descricao"));
        imovel.setTipo(rs.getString("tipo"));
        imovel.setFinalidade(rs.getString("finalidade"));
        imovel.setValorVenda(rs.getDouble("valor_venda"));
        imovel.setValorAluguel(rs.getDouble("valor_aluguel"));
        imovel.setValorCondominio(rs.getDouble("valor_condominio"));
        imovel.setValorIptu(rs.getDouble("valor_iptu"));
        imovel.setAreaTotal(rs.getDouble("area_total"));
        imovel.setAreaConstruida(rs.getDouble("area_construida"));
        imovel.setQuartos(rs.getInt("quartos"));
        imovel.setSuites(rs.getInt("suites"));
        imovel.setBanheiros(rs.getInt("banheiros"));
        imovel.setVagasGaragem(rs.getInt("vagas_garagem"));
        imovel.setAndar(rs.getInt("andar"));
        imovel.setAceitaPermuta(rs.getBoolean("aceita_permuta"));
        imovel.setAceitaFinanciamento(rs.getBoolean("aceita_financiamento"));
        imovel.setMobiliado(rs.getBoolean("mobiliado"));
        imovel.setStatus(rs.getString("status"));
        imovel.setDestaque(rs.getBoolean("destaque"));

        // Carrega relacionamentos
        int idEndereco = rs.getInt("id_endereco");
        if (idEndereco > 0) {
            Endereco endereco = enderecoDAO.buscarPorId(idEndereco);
            imovel.setEndereco(endereco);
        }

        int idProprietario = rs.getInt("id_proprietario");
        if (idProprietario > 0 && !rs.wasNull()) {
            PessoaProprietario proprietario = proprietarioDAO.buscarPorId(idProprietario);
            imovel.setProprietario(proprietario);
        }

        int idCorretor = rs.getInt("id_corretor_responsavel");
        if (idCorretor > 0 && !rs.wasNull()) {
            PessoaCorretor corretor = corretorDAO.buscarPorId(idCorretor);
            imovel.setCorretorResponsavel(corretor);
        }

        return imovel;
    }
}
