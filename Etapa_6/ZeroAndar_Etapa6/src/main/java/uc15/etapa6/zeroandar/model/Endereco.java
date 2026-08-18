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

/**
 * Representa um endereco no sistema ZeroAndar. Usado para pessoas e imoveis.
 */
public class Endereco {

    private int idEndereco;
    private String rua;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
    private String pontoReferencia;

    public Endereco() {
    }

    public Endereco(String rua, String numero, String bairro, String cidade, String estado, String cep) {
        this.rua = rua;
        this.numero = numero;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.cep = cep;
    }

    public int getIdEndereco() {
        return idEndereco;
    }

    public void setIdEndereco(int idEndereco) {
        this.idEndereco = idEndereco;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getPontoReferencia() {
        return pontoReferencia;
    }

    public void setPontoReferencia(String pontoReferencia) {
        this.pontoReferencia = pontoReferencia;
    }

    // Retorna endereoo formatado
    public String getEnderecoCompleto() {
        StringBuilder sb = new StringBuilder();
        sb.append(rua);
        if (numero != null && !numero.isEmpty()) {
            sb.append(", ").append(numero);
        }
        if (complemento != null && !complemento.isEmpty()) {
            sb.append(" - ").append(complemento);
        }
        sb.append(" - ").append(bairro);
        sb.append(" - ").append(cidade).append("/").append(estado);
        sb.append(" - CEP: ").append(cep);
        return sb.toString();
    }

    @Override
    public String toString() {
        return getEnderecoCompleto();
    }
}
