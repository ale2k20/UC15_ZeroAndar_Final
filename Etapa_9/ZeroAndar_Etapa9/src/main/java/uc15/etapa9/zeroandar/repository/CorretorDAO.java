/**
 * UC15 - Projeto Integrador 3 - Etapa 9
 *
 * @author Alex
 * @since 15 de agosto de 2026
 * @version 1.9
 */
package uc15.etapa9.zeroandar.repository;

import java.sql.SQLException;
import java.util.List;
import uc15.etapa9.zeroandar.model.PessoaCorretor;

/**
 * Interface que define o contrato de acesso a dados de Corretores.
 *
 * Aplicacao do principio SOLID: - D (Dependency Inversion) - I (Interface
 * Segregation)
 */
public interface CorretorDAO {

    int inserir(PessoaCorretor corretor) throws SQLException;

    boolean atualizar(PessoaCorretor corretor) throws SQLException;

    boolean excluir(String creci) throws SQLException;

    PessoaCorretor buscarPorId(int idCorretor) throws SQLException;

    PessoaCorretor buscarPorCpf(String cpf) throws SQLException;

    PessoaCorretor buscarPorCreci(String creci) throws SQLException;

    List<PessoaCorretor> listarTodos() throws SQLException;

    List<PessoaCorretor> listarAtivos() throws SQLException;

    List<PessoaCorretor> buscarPorEspecialidade(String especialidade) throws SQLException;

    List<PessoaCorretor> buscarPorTexto(String texto) throws SQLException;

    int contarTotal() throws SQLException;

    int contarOnline() throws SQLException;
}
