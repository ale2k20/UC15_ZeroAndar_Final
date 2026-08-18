/**
 * UC15 - Projeto Integrador 3 - Etapa 8
 *
 * @author Alex
 * @since 15 de agosto de 2026
 * @version 1.8
 */
package uc15.etapa8.zeroandar.repository;

import java.sql.SQLException;
import java.util.List;
import uc15.etapa8.zeroandar.model.Imovel;

/**
 * Interface que define o contrato de acesso a dados de Imoveis.
 *
 * Aplicacao do principio SOLID: - D (Dependency Inversion): controllers
 * dependem desta interface, nao da implementacao concreta ImovelDAO. - I
 * (Interface Segregation): interface especifica para Imovel, sem metodos de
 * outras entidades.
 */
public interface ImovelDAO {

    int inserir(Imovel imovel) throws SQLException;

    boolean atualizar(Imovel imovel) throws SQLException;

    boolean excluir(String codigoImovel) throws SQLException;

    Imovel buscarPorCodigo(String codigoImovel) throws SQLException;

    Imovel buscarPorId(int idImovel) throws SQLException;

    List<Imovel> listarTodos() throws SQLException;

    List<Imovel> listarDisponiveis() throws SQLException;

    List<Imovel> listarDestaques() throws SQLException;

    List<Imovel> buscarPorTipo(String tipo) throws SQLException;

    List<Imovel> buscarPorFinalidade(String finalidade) throws SQLException;

    List<Imovel> buscarPorStatus(String status) throws SQLException;

    int contarTotal() throws SQLException;

    long contarPorStatus(String status) throws SQLException;
}
