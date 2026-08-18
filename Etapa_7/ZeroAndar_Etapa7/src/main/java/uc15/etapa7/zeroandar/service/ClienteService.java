/**
 * UC15 - Projeto Integrador 3 - Etapa 7
 *
 * @author Alexandre
 * @since 14 de agosto de 2026
 * @version 1.7
 */
package uc15.etapa7.zeroandar.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import uc15.etapa7.zeroandar.model.PessoaCliente;
import uc15.etapa7.zeroandar.repository.ClienteDAO;
import uc15.etapa7.zeroandar.repository.jdbc.JdbcClienteDAO;

public class ClienteService {

    private final ClienteDAO clienteDAO;
    private final ValidacaoService validacao;

    /**
     * Construtor padrao — usa implementacao MySQL.
     */
    public ClienteService() {
        this.clienteDAO = new JdbcClienteDAO();
        this.validacao = new ValidacaoService();
    }

    /**
     * Construtor para injecao de dependencia (facilita testes futuros).
     *
     * @param clienteDAO implementacao do DAO a utilizar
     */
    public ClienteService(ClienteDAO clienteDAO) {
        this.clienteDAO = clienteDAO;
        this.validacao = new ValidacaoService();
    }

    /**
     * Adiciona um novo cliente apos validar os dados.
     *
     * @param cliente objeto PessoaCliente a ser persistido
     * @return true se cadastrado com sucesso, false caso contrario
     */
    public boolean adicionar(PessoaCliente cliente) {
        if (!validarDados(cliente)) {
            return false;
        }
        try {
            if (cliente.getDataCadastro() == null) {
                cliente.setDataCadastro(new Date());
            }
            int id = clienteDAO.inserir(cliente);
            if (id > 0) {
                cliente.setIdPessoaCliente(id);
                return true;
            }
            return false;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Atualiza os dados de um cliente existente.
     *
     * @param cliente objeto com os dados atualizados
     * @return true se atualizado com sucesso, false caso contrario
     */
    public boolean atualizar(PessoaCliente cliente) {
        if (!validarDados(cliente)) {
            return false;
        }
        try {
            return clienteDAO.atualizar(cliente);
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Exclui (ou inativa) um cliente pelo ID.
     *
     * @param idCliente ID do cliente a excluir
     * @return true se excluido com sucesso, false caso contrario
     */
    public boolean excluir(int idCliente) {
        if (idCliente <= 0) {
            return false;
        }
        try {
            return clienteDAO.excluir(idCliente);
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Busca cliente pelo ID.
     *
     * @param idCliente ID do cliente
     * @return PessoaCliente encontrado ou null
     */
    public PessoaCliente buscarPorId(int idCliente) {
        try {
            return clienteDAO.buscarPorId(idCliente);
        } catch (SQLException e) {
            return null;
        }
    }

    /**
     * Busca cliente pelo CPF.
     *
     * @param cpf CPF do cliente
     * @return PessoaCliente encontrado ou null
     */
    public PessoaCliente buscarPorCpf(String cpf) {
        try {
            return clienteDAO.buscarPorCpf(cpf);
        } catch (SQLException e) {
            return null;
        }
    }

    /**
     * Lista todos os clientes cadastrados.
     *
     * @return lista de clientes (vazia se nenhum encontrado)
     */
    public List<PessoaCliente> listarTodos() {
        try {
            return clienteDAO.listarTodos();
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Lista apenas clientes com status ativo.
     *
     * @return lista de clientes ativos
     */
    public List<PessoaCliente> listarAtivos() {
        try {
            return clienteDAO.listarAtivos();
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Busca clientes cujo nome contenha o termo informado.
     *
     * @param nome termo de busca
     * @return lista de clientes encontrados
     */
    public List<PessoaCliente> buscarPorNome(String nome) {
        try {
            return clienteDAO.buscarPorNome(nome);
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Conta o total de clientes cadastrados.
     *
     * @return total de clientes
     */
    public int contarTotal() {
        try {
            return clienteDAO.contarTotal();
        } catch (SQLException e) {
            return 0;
        }
    }

    /**
     * Conta apenas os clientes ativos.
     *
     * @return total de clientes ativos
     */
    public int contarAtivos() {
        try {
            return clienteDAO.contarAtivos();
        } catch (SQLException e) {
            return 0;
        }
    }

    /**
     * Valida os dados obrigatorios do cliente antes de persistir. Delega ao
     * ValidacaoService — aplicacao do principio S.
     *
     * @param cliente objeto a validar
     * @return true se os dados estao validos
     */
    private boolean validarDados(PessoaCliente cliente) {
        if (cliente == null) {
            return false;
        }
        if (!validacao.validarNome(cliente.getNome())) {
            return false;
        }
        if (!validacao.validarCpf(cliente.getCpf())) {
            return false;
        }
        if (cliente.getEmail() != null && !cliente.getEmail().isEmpty()) {
            if (!validacao.validarEmail(cliente.getEmail())) {
                return false;
            }
        }
        return true;
    }
}
