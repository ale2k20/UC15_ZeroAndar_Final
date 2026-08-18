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

import uc15.etapa9.zeroandar.model.PessoaProprietario;
import uc15.etapa9.zeroandar.repository.ProprietarioDAO;
import uc15.etapa9.zeroandar.repository.jdbc.JdbcProprietarioDAO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Service para gerenciar operacoes de Proprietarios
 * Agora com persistencia no banco de dados MySQL
 */

public class ProprietarioService {
    
    private final ProprietarioDAO proprietarioDAO;
    
    public ProprietarioService() {
        this.proprietarioDAO = new JdbcProprietarioDAO();
    }
    
    /**
     * Adiciona um novo proprietario
     */
    public boolean adicionar(PessoaProprietario proprietario) {
        try {
            if (proprietario.getDataCadastro() == null) {
                proprietario.setDataCadastro(new Date());
            }
            
            int id = proprietarioDAO.inserir(proprietario);
            
            return id > 0;
        } catch (SQLException e) { return false; }
    }
    
    /**
     * Atualiza dados de um proprietario existente
     */
    public boolean atualizar(PessoaProprietario proprietario) {
        try {
            boolean sucesso = proprietarioDAO.atualizar(proprietario);
            
            return sucesso;
        } catch (SQLException e) { return false; }
    }
    
    /**
     * Exclui um proprietario (soft delete)
     */
    public boolean excluir(int idProprietario) {
        try {
            boolean sucesso = proprietarioDAO.excluir(idProprietario);
            
            return sucesso;
        } catch (SQLException e) { return false; }
    }
    
    /**
     * Busca proprietario por ID
     */
    public PessoaProprietario buscarPorId(int idProprietario) {
        try {
            return proprietarioDAO.buscarPorId(idProprietario);
        } catch (SQLException e) { return null; }
    }
    
    /**
     * Lista todos os proprietarios
     */
    public List<PessoaProprietario> listarTodos() {
        try {
            return proprietarioDAO.listarTodos();
        } catch (SQLException e) { return new ArrayList<>(); }
    }
    
    /**
     * Lista proprietarios que aceitam contato
     */
    public List<PessoaProprietario> buscarPorTexto(String texto) {
        try { return proprietarioDAO.buscarPorTexto(texto); } catch (SQLException e) { return new ArrayList<>(); }
    }

    public List<PessoaProprietario> listarQueAceitamContato() {
        try {
            return proprietarioDAO.listarQueAceitamContato();
        } catch (SQLException e) { return new ArrayList<>(); }
    }
    
    /**
     * Conta total de proprietarios
     */
    public int contarTotal() {
        try {
            return proprietarioDAO.contarTotal();
        } catch (SQLException e) { return 0; }
    }
}

