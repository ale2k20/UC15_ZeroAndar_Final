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
 * Representa um cliente no sistema. Cliente busca imoveis para compra ou
 * aluguel.
 */
public class PessoaCliente extends Pessoa {

    private int idPessoaCliente;
    private Date dataCadastro;
    private String origemCadastro; // Ex: "Site", "Indicacao", "Corretor"
    private int scoreInteresse; // 0 a 100

    public PessoaCliente() {
        super();
        this.dataCadastro = new Date();
    }

    public PessoaCliente(String nome, String cpf, String email) {
        super(nome, cpf, email);
        this.dataCadastro = new Date();
    }

    public int getIdPessoaCliente() {
        return idPessoaCliente;
    }

    public void setIdPessoaCliente(int idPessoaCliente) {
        this.idPessoaCliente = idPessoaCliente;
    }

    public Date getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public String getOrigemCadastro() {
        return origemCadastro;
    }

    public void setOrigemCadastro(String origemCadastro) {
        this.origemCadastro = origemCadastro;
    }

    public int getScoreInteresse() {
        return scoreInteresse;
    }

    public void setScoreInteresse(int scoreInteresse) {
        this.scoreInteresse = scoreInteresse;
    }

    @Override
    public String toString() {
        return "Cliente: " + super.toString() + " | Score: " + scoreInteresse;
    }
}
