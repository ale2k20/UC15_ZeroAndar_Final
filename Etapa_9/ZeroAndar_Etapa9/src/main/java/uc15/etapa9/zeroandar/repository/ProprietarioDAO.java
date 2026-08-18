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
import uc15.etapa9.zeroandar.model.PessoaProprietario;

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

    List<PessoaProprietario> buscarPorTexto(String texto) throws SQLException;

    int contarTotal() throws SQLException;
}
