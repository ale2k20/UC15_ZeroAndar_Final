/**
 * UC15 - Projeto Integrador 3 - Etapa 6
 *
 * @author Alex
 * @since 14 de agosto de 2026
 * @version 1.6
 */
package uc15.etapa6.zeroandar.repository;

import java.sql.SQLException;
import java.util.List;
import uc15.etapa6.zeroandar.model.PessoaProprietario;

/**
 * Interface que define o contrato de acesso a dados de Proprietarios.
 *
 * Aplicacao do principio SOLID: - D (Dependency Inversion) - I (Interface
 * Segregation)
 */
public interface ProprietarioDAO {

    int inserir(PessoaProprietario proprietario) throws SQLException;

    boolean atualizar(PessoaProprietario proprietario) throws SQLException;

    boolean excluir(int idProprietario) throws SQLException;

    PessoaProprietario buscarPorId(int idProprietario) throws SQLException;

    PessoaProprietario buscarPorCpf(String cpf) throws SQLException;

    List<PessoaProprietario> listarTodos() throws SQLException;

    List<PessoaProprietario> listarAtivos() throws SQLException;

    List<PessoaProprietario> listarQueAceitamContato() throws SQLException;

    int contarTotal() throws SQLException;
}
