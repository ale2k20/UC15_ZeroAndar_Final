/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 * UC15 - Projeto Integrador 3 - Etapa 6
 *
 * @author Alex
 * @since 14 de agosto de 2026
 * @version 1.6
 */
package uc15.etapa6.zeroandar.model;

import java.util.Date;

/**
 * Representa um corretor no sistema. Corretor gerencia imoveis e intermedia
 * negociacoes.
 */
public class PessoaCorretor extends Pessoa {

    private int idPessoaCorretor;
    private String creci;
    private double comissaoPercentual;
    private Date dataAdmissao;
    private Date dataDesligamento;
    private String especialidade; // Ex: "Residencial", "Comercial"
    private double metaMensal;

    public PessoaCorretor() {
        super();
        this.comissaoPercentual = 6.0;
    }

    public PessoaCorretor(String nome, String cpf, String creci) {
        super(nome, cpf, null);
        this.creci = creci;
        this.comissaoPercentual = 6.0;
    }

    public int getIdPessoaCorretor() {
        return idPessoaCorretor;
    }

    public void setIdPessoaCorretor(int idPessoaCorretor) {
        this.idPessoaCorretor = idPessoaCorretor;
    }

    public String getCreci() {
        return creci;
    }

    public void setCreci(String creci) {
        this.creci = creci;
    }

    public double getComissaoPercentual() {
        return comissaoPercentual;
    }

    public void setComissaoPercentual(double comissaoPercentual) {
        this.comissaoPercentual = comissaoPercentual;
    }

    public Date getDataAdmissao() {
        return dataAdmissao;
    }

    public void setDataAdmissao(Date dataAdmissao) {
        this.dataAdmissao = dataAdmissao;
    }

    public Date getDataDesligamento() {
        return dataDesligamento;
    }

    public void setDataDesligamento(Date dataDesligamento) {
        this.dataDesligamento = dataDesligamento;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public double getMetaMensal() {
        return metaMensal;
    }

    public void setMetaMensal(double metaMensal) {
        this.metaMensal = metaMensal;
    }

    // TODO: Implementar calculo de comissao na Etapa 4
    public double calcularComissao(double valorVenda) {
        return valorVenda * (comissaoPercentual / 100);
    }

    @Override
    public String toString() {
        return "Corretor: " + super.toString() + " | CRECI: " + creci;
    }
}
