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
package uc15.etapa7.zeroandar.model;

import java.util.Date;

/**
 * Representa um proprietario de imoveis no sistema.
 */
public class PessoaProprietario extends Pessoa {

    private int idPessoaProprietario;
    private Date dataCadastro;
    private boolean aceitaContato;

    public PessoaProprietario() {
        super();
        this.dataCadastro = new Date();
        this.aceitaContato = true;
    }

    public PessoaProprietario(String nome, String cpf) {
        super(nome, cpf, null);
        this.dataCadastro = new Date();
        this.aceitaContato = true;
    }

    public int getIdPessoaProprietario() {
        return idPessoaProprietario;
    }

    public void setIdPessoaProprietario(int idPessoaProprietario) {
        this.idPessoaProprietario = idPessoaProprietario;
    }

    public Date getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public boolean isAceitaContato() {
        return aceitaContato;
    }

    public void setAceitaContato(boolean aceitaContato) {
        this.aceitaContato = aceitaContato;
    }

    @Override
    public String toString() {
        return "Proprietário: " + super.toString();
    }
}
