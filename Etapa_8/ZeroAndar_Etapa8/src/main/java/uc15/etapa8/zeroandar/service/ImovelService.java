/**
 * UC15 - Projeto Integrador 3 - Etapa 8
 *
 * @author Alex
 * @since 15 de agosto de 2026
 * @version 1.8
 */
package uc15.etapa8.zeroandar.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import uc15.etapa8.zeroandar.model.Imovel;
import uc15.etapa8.zeroandar.repository.ImovelDAO;
import uc15.etapa8.zeroandar.repository.jdbc.JdbcImovelDAO;

public class ImovelService {

    private final ImovelDAO imovelDAO;
    private final ValidacaoService validacao;

    public ImovelService() {
        this.imovelDAO = new JdbcImovelDAO();
        this.validacao = new ValidacaoService();
    }

    public ImovelService(ImovelDAO imovelDAO) {
        this.imovelDAO = imovelDAO;
        this.validacao = new ValidacaoService();
    }

    /**
     * Adiciona novo imovel apos validar os dados.
     *
     * @param imovel objeto Imovel a persistir
     * @return true se cadastrado com sucesso
     */
    public boolean adicionar(Imovel imovel) {
        if (!validarDados(imovel)) {
            return false;
        }
        try {
            int id = imovelDAO.inserir(imovel);
            if (id > 0) {
                imovel.setIdImovel(id);
                return true;
            }
            return false;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Atualiza os dados de um imovel existente.
     *
     * @param imovel objeto com dados atualizados
     * @return true se atualizado com sucesso
     */
    public boolean atualizar(Imovel imovel) {
        if (!validarDados(imovel)) {
            return false;
        }
        try {
            return imovelDAO.atualizar(imovel);
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Exclui imovel pelo codigo.
     *
     * @param codigoImovel codigo unico do imovel
     * @return true se excluido com sucesso
     */
    public boolean excluir(String codigoImovel) {
        if (codigoImovel == null || codigoImovel.trim().isEmpty()) {
            return false;
        }
        try {
            return imovelDAO.excluir(codigoImovel);
        } catch (SQLException e) {
            return false;
        }
    }

    public Imovel buscarPorCodigo(String codigoImovel) {
        try {
            return imovelDAO.buscarPorCodigo(codigoImovel);
        } catch (SQLException e) {
            return null;
        }
    }

    public List<Imovel> listarTodos() {
        try {
            return imovelDAO.listarTodos();
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }

    public List<Imovel> listarDisponiveis() {
        try {
            return imovelDAO.listarDisponiveis();
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }

    public List<Imovel> listarDestaques() {
        try {
            return imovelDAO.listarDestaques();
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }

    public List<Imovel> buscarPorTipo(String tipo) {
        try {
            return imovelDAO.buscarPorTipo(tipo);
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }

    public List<Imovel> buscarPorFinalidade(String finalidade) {
        try {
            return imovelDAO.buscarPorFinalidade(finalidade);
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }

    public List<Imovel> buscarPorStatus(String status) {
        try {
            return imovelDAO.buscarPorStatus(status);
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }

    public int contarTotal() {
        try {
            return imovelDAO.contarTotal();
        } catch (SQLException e) {
            return 0;
        }
    }

    public long contarPorStatus(String status) {
        try {
            return imovelDAO.contarPorStatus(status);
        } catch (SQLException e) {
            return 0;
        }
    }

    /**
     * Valida os dados obrigatorios do imovel.
     */
    private boolean validarDados(Imovel imovel) {
        if (imovel == null) {
            return false;
        }
        if (imovel.getCodigoImovel() == null || imovel.getCodigoImovel().trim().isEmpty()) {
            return false;
        }
        if (imovel.getTipo() == null || imovel.getTipo().trim().isEmpty()) {
            return false;
        }
        if (!validacao.validarValor(imovel.getValorVenda())
                && !validacao.validarValor(imovel.getValorAluguel())) {
            return false;
        }
        return true;
    }
}
