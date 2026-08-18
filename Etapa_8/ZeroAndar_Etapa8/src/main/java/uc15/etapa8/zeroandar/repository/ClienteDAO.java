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
import uc15.etapa8.zeroandar.model.PessoaCliente;

/**
 * Interface que define o contrato de acesso a dados de Clientes.
 *
 * Aplicacao do principio SOLID: - D (Dependency Inversion): controllers
 * dependem desta interface, nao da implementacao concreta ClienteDAO. - I
 * (Interface Segregation): interface especifica para Cliente, sem metodos de
 * outras entidades.
 *
 * Vantagem: no futuro, e possivel criar ClienteDAOPostgres ou ClienteDAOMemoria
 * (para testes) sem alterar nada nos controllers.
 */
public interface ClienteDAO {

    int inserir(PessoaCliente cliente) throws SQLException;

    boolean atualizar(PessoaCliente cliente) throws SQLException;

    boolean excluir(int idCliente) throws SQLException;

    PessoaCliente buscarPorId(int idCliente) throws SQLException;

    PessoaCliente buscarPorCpf(String cpf) throws SQLException;

    List<PessoaCliente> listarTodos() throws SQLException;

    List<PessoaCliente> listarAtivos() throws SQLException;

    List<PessoaCliente> buscarPorNome(String nome) throws SQLException;

    int contarTotal() throws SQLException;

    int contarAtivos() throws SQLException;
}
