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
 * Representa um imovel no sistema.
 */
public class Imovel {

    private int idImovel;
    private String codigoImovel;
    private String descricao;
    private String tipo;        // Ex: "Apartamento", "Casa", "Terreno"
    private String finalidade;  // "Venda", "Aluguel", "Ambos"
    private double valorVenda;
    private double valorAluguel;
    private double valorCondominio;
    private double valorIptu;
    private double areaTotal;
    private double areaConstruida;
    private int quartos;
    private int suites;
    private int banheiros;
    private int vagasGaragem;
    private int andar;
    private boolean aceitaPermuta;
    private boolean aceitaFinanciamento;
    private boolean mobiliado;
    private String status; // "Disponivel", "Vendido", "Alugado", "Inativo"
    private boolean destaque;
    private PessoaProprietario proprietario;
    private PessoaCorretor corretorResponsavel;
    private Endereco endereco;

    public Imovel() {
        this.status = "disponivel";
        this.aceitaFinanciamento = true;
    }

    public Imovel(String codigoImovel, String tipo, PessoaProprietario proprietario) {
        this.codigoImovel = codigoImovel;
        this.tipo = tipo;
        this.proprietario = proprietario;
        this.status = "disponivel";
        this.aceitaFinanciamento = true;
    }

    public int getIdImovel() {
        return idImovel;
    }

    public void setIdImovel(int idImovel) {
        this.idImovel = idImovel;
    }

    public String getCodigoImovel() {
        return codigoImovel;
    }

    public void setCodigoImovel(String codigoImovel) {
        this.codigoImovel = codigoImovel;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getFinalidade() {
        return finalidade;
    }

    public void setFinalidade(String finalidade) {
        this.finalidade = finalidade;
    }

    public double getValorVenda() {
        return valorVenda;
    }

    public void setValorVenda(double valorVenda) {
        this.valorVenda = valorVenda;
    }

    public double getValorAluguel() {
        return valorAluguel;
    }

    public void setValorAluguel(double valorAluguel) {
        this.valorAluguel = valorAluguel;
    }

    public double getValorCondominio() {
        return valorCondominio;
    }

    public void setValorCondominio(double valorCondominio) {
        this.valorCondominio = valorCondominio;
    }

    public double getValorIptu() {
        return valorIptu;
    }

    public void setValorIptu(double valorIptu) {
        this.valorIptu = valorIptu;
    }

    public double getAreaTotal() {
        return areaTotal;
    }

    public void setAreaTotal(double areaTotal) {
        this.areaTotal = areaTotal;
    }

    public double getAreaConstruida() {
        return areaConstruida;
    }

    public void setAreaConstruida(double areaConstruida) {
        this.areaConstruida = areaConstruida;
    }

    public int getQuartos() {
        return quartos;
    }

    public void setQuartos(int quartos) {
        this.quartos = quartos;
    }

    public int getSuites() {
        return suites;
    }

    public void setSuites(int suites) {
        this.suites = suites;
    }

    public int getBanheiros() {
        return banheiros;
    }

    public void setBanheiros(int banheiros) {
        this.banheiros = banheiros;
    }

    public int getVagasGaragem() {
        return vagasGaragem;
    }

    public void setVagasGaragem(int vagasGaragem) {
        this.vagasGaragem = vagasGaragem;
    }

    public int getAndar() {
        return andar;
    }

    public void setAndar(int andar) {
        this.andar = andar;
    }

    public boolean isAceitaPermuta() {
        return aceitaPermuta;
    }

    public void setAceitaPermuta(boolean aceitaPermuta) {
        this.aceitaPermuta = aceitaPermuta;
    }

    public boolean isAceitaFinanciamento() {
        return aceitaFinanciamento;
    }

    public void setAceitaFinanciamento(boolean aceitaFinanciamento) {
        this.aceitaFinanciamento = aceitaFinanciamento;
    }

    public boolean isMobiliado() {
        return mobiliado;
    }

    public void setMobiliado(boolean mobiliado) {
        this.mobiliado = mobiliado;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isDestaque() {
        return destaque;
    }

    public void setDestaque(boolean destaque) {
        this.destaque = destaque;
    }

    public PessoaProprietario getProprietario() {
        return proprietario;
    }

    public void setProprietario(PessoaProprietario proprietario) {
        this.proprietario = proprietario;
    }

    public PessoaCorretor getCorretorResponsavel() {
        return corretorResponsavel;
    }

    public void setCorretorResponsavel(PessoaCorretor corretorResponsavel) {
        this.corretorResponsavel = corretorResponsavel;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    // TODO: Implementar calculo de valor total nas proximas etapas
    public double calcularValorTotal() {
        return valorVenda + valorIptu;
    }

    @Override
    public String toString() {
        return "Código: " + codigoImovel + " | Tipo: " + tipo
                + " | Quartos: " + quartos + " | Valor: R$ " + valorVenda;
    }
}
