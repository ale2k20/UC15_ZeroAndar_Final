/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 * UC15 - Projeto Integrador 3 - Etapa 9
 *
 * @author Alex
 * @since 15 de agosto de 2026
 * @version 1.9
 */

package uc15.etapa9.zeroandar.service;

import uc15.etapa9.zeroandar.model.PessoaCorretor;
import uc15.etapa9.zeroandar.repository.CorretorDAO;
import uc15.etapa9.zeroandar.repository.jdbc.JdbcCorretorDAO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *  Service de Corretores
 */

public class CorretorService {
    
    private final CorretorDAO corretorDAO;
    
    public CorretorService() {
        this.corretorDAO = new JdbcCorretorDAO();
    }
    
    public boolean adicionar(PessoaCorretor corretor) {
        try {
            if (corretor.getDataAdmissao() == null) {
                corretor.setDataAdmissao(new Date());
            }
            
            int id = corretorDAO.inserir(corretor);
            
            return id > 0;
        } catch (SQLException e) { return false; }
    }
    
    public boolean atualizar(PessoaCorretor corretor) {
        try {
            boolean sucesso = corretorDAO.atualizar(corretor);
            return sucesso;
        } catch (SQLException e) { return false; }
    }
    
    public boolean excluir(String creci) {
        try {
            boolean sucesso = corretorDAO.excluir(creci);
            return sucesso;
        } catch (SQLException e) { return false; }
    }
    
    public PessoaCorretor buscarPorId(int idCorretor) {
        try {
            return ((uc15.etapa9.zeroandar.repository.CorretorDAO) corretorDAO).buscarPorId(idCorretor);
        } catch (SQLException e) {
            return null;
        }
    }

    public PessoaCorretor buscarPorCreci(String creci) {
        try {
            return corretorDAO.buscarPorCreci(creci);
        } catch (SQLException e) { return null; }
    }
    
    public List<PessoaCorretor> listarTodos() {
        try {
            return corretorDAO.listarTodos();
        } catch (SQLException e) { return new ArrayList<>(); }
    }
    
    public List<PessoaCorretor> buscarPorTexto(String texto) {
        try { return corretorDAO.buscarPorTexto(texto); } catch (SQLException e) { return new ArrayList<>(); }
    }

    public List<PessoaCorretor> buscarPorEspecialidade(String especialidade) {
        try {
            return corretorDAO.buscarPorEspecialidade(especialidade);
        } catch (SQLException e) { return new ArrayList<>(); }
    }
    
    public List<PessoaCorretor> listarAtivos() {
        try {
            return corretorDAO.listarAtivos();
        } catch (SQLException e) { return new ArrayList<>(); }
    }
    
    public int contarTotal() {
        try {
            return corretorDAO.contarTotal();
        } catch (SQLException e) { return 0; }
    }
    
    public int contarOnline() {
        try {
            return corretorDAO.contarOnline();
        } catch (SQLException e) { return 0; }
    }
}

