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

/**
 * Data Access Object para Endereco
 */
public class JdbcEnderecoDAO {

    private Connection connection;

    public JdbcEnderecoDAO() {
        try {
            this.connection = DatabaseConnection.getInstance().getConnection();
        } catch (SQLException e) {
            System.err.println("Erro ao obter conexão: " + e.getMessage());
        }
    }

    /**
     * Insere um novo endereco no banco
     */
    public int inserir(Endereco endereco) throws SQLException {
        String sql = "INSERT INTO Endereco (rua, numero, complemento, bairro, cidade, "
                + "estado, cep, ponto_referencia, latitude, longitude) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, endereco.getRua());
            stmt.setString(2, endereco.getNumero());
            stmt.setString(3, endereco.getComplemento());
            stmt.setString(4, endereco.getBairro());
            stmt.setString(5, endereco.getCidade());
            stmt.setString(6, endereco.getEstado());
            stmt.setString(7, endereco.getCep());
            stmt.setString(8, endereco.getPontoReferencia());
            stmt.setObject(9, null); // latitude
            stmt.setObject(10, null); // longitude

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    /**
     * Atualiza um endereco existente
     */
    public boolean atualizar(Endereco endereco) throws SQLException {
        String sql = "UPDATE Endereco SET rua = ?, numero = ?, complemento = ?, "
                + "bairro = ?, cidade = ?, estado = ?, cep = ?, ponto_referencia = ? "
                + "WHERE id_endereco = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, endereco.getRua());
            stmt.setString(2, endereco.getNumero());
            stmt.setString(3, endereco.getComplemento());
            stmt.setString(4, endereco.getBairro());
            stmt.setString(5, endereco.getCidade());
            stmt.setString(6, endereco.getEstado());
            stmt.setString(7, endereco.getCep());
            stmt.setString(8, endereco.getPontoReferencia());
            stmt.setInt(9, endereco.getIdEndereco());

            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Busca endereco por ID
     */
    public Endereco buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Endereco WHERE id_endereco = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extrairEndereco(rs);
            }
        }
        return null;
    }

    /**
     * Lista todos os enderecos
     */
    public List<Endereco> listarTodos() throws SQLException {
        List<Endereco> lista = new ArrayList<>();
        String sql = "SELECT * FROM Endereco ORDER BY cidade, bairro";

        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(extrairEndereco(rs));
            }
        }
        return lista;
    }

    /**
     * Busca enderecos por cidade
     */
    public List<Endereco> buscarPorCidade(String cidade) throws SQLException {
        List<Endereco> lista = new ArrayList<>();
        String sql = "SELECT * FROM Endereco WHERE cidade LIKE ? ORDER BY bairro";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + cidade + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(extrairEndereco(rs));
            }
        }
        return lista;
    }

    /**
     * Exclui um endereco
     */
    public boolean excluir(int id) throws SQLException {
        String sql = "DELETE FROM Endereco WHERE id_endereco = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Extrai objeto Endereco do ResultSet
     */
    private Endereco extrairEndereco(ResultSet rs) throws SQLException {
        Endereco endereco = new Endereco();
        endereco.setIdEndereco(rs.getInt("id_endereco"));
        endereco.setRua(rs.getString("rua"));
        endereco.setNumero(rs.getString("numero"));
        endereco.setComplemento(rs.getString("complemento"));
        endereco.setBairro(rs.getString("bairro"));
        endereco.setCidade(rs.getString("cidade"));
        endereco.setEstado(rs.getString("estado"));
        endereco.setCep(rs.getString("cep"));
        endereco.setPontoReferencia(rs.getString("ponto_referencia"));
        return endereco;
    }
}
