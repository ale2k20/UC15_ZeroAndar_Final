package uc15.etapa9.zeroandar.web;

import java.sql.Date;
import java.util.Map;
import uc15.etapa9.zeroandar.model.Endereco;
import uc15.etapa9.zeroandar.model.Imovel;
import uc15.etapa9.zeroandar.model.PessoaCliente;
import uc15.etapa9.zeroandar.model.PessoaCorretor;
import uc15.etapa9.zeroandar.model.PessoaProprietario;

/**
 * Converte os dados simples enviados pelo JavaScript para os modelos Java.
 * A classe evita colocar codigo de leitura de JSON dentro dos Controllers.
 */
public final class WebDataMapper {

    private WebDataMapper() { }

    public static String text(Map<String, Object> data, String key) {
        Object value = data.get(key);
        return value == null ? null : String.valueOf(value).trim();
    }

    public static int integer(Map<String, Object> data, String key) {
        Object value = data.get(key);
        if (value == null || String.valueOf(value).isBlank()) return 0;
        return Integer.parseInt(String.valueOf(value));
    }

    public static double decimal(Map<String, Object> data, String key) {
        Object value = data.get(key);
        if (value == null || String.valueOf(value).isBlank()) return 0.0;
        return Double.parseDouble(String.valueOf(value));
    }

    public static boolean bool(Map<String, Object> data, String key) {
        Object value = data.get(key);
        return value != null && Boolean.parseBoolean(String.valueOf(value));
    }

    public static Date date(Map<String, Object> data, String key) {
        String value = text(data, key);
        return value == null || value.isBlank() ? null : Date.valueOf(value);
    }

    public static Endereco endereco(Map<String, Object> data) {
        String rua = text(data, "rua");
        String numero = text(data, "numero");
        String complemento = text(data, "complemento");
        String bairro = text(data, "bairro");
        String cidade = text(data, "cidade");
        String estado = text(data, "estado");
        String cep = text(data, "cep");

        // Endereco e opcional nas telas de Cliente, Proprietario e Corretor.
        // Se o usuario nao informou nenhum dado de endereco, nao criamos
        // um registro vazio que depois falharia no banco.
        if ((rua == null || rua.isBlank()) && (numero == null || numero.isBlank())
                && (complemento == null || complemento.isBlank())
                && (bairro == null || bairro.isBlank()) && (cidade == null || cidade.isBlank())
                && (estado == null || estado.isBlank()) && (cep == null || cep.isBlank())) {
            return null;
        }

        Endereco e = new Endereco();
        e.setRua(rua == null ? "" : rua);
        e.setNumero(numero);
        e.setComplemento(complemento);
        // O formulario atual nao possui bairro e CEP. O banco exige os
        // campos, portanto vazio e usado quando nao foram informados.
        e.setBairro(bairro == null ? "" : bairro);
        e.setCidade(cidade == null ? "" : cidade);
        e.setEstado(estado == null ? "" : estado);
        e.setCep(cep == null ? "" : cep);
        return e;
    }

    public static PessoaCliente cliente(Map<String, Object> d) {
        PessoaCliente p = new PessoaCliente();
        if (d.containsKey("idPessoa")) p.setIdPessoa(integer(d, "idPessoa"));
        if (d.containsKey("idPessoaCliente")) p.setIdPessoaCliente(integer(d, "idPessoaCliente"));
        p.setNome(text(d, "nome"));
        p.setCpf(text(d, "cpf"));
        p.setRg(text(d, "rg"));
        p.setEmail(text(d, "email"));
        p.setTelefone(text(d, "telefone"));
        p.setDataNascimento(date(d, "dataNascimento"));
        p.setEndereco(endereco(d));
        p.setObservacoes(text(d, "observacoes"));
        p.setAtivo(!d.containsKey("ativo") || bool(d, "ativo"));
        p.setOrigemCadastro(text(d, "origemCadastro"));
        p.setScoreInteresse(integer(d, "scoreInteresse"));
        p.setDataCadastro(date(d, "dataCadastro"));
        return p;
    }

    public static PessoaProprietario proprietario(Map<String, Object> d) {
        PessoaProprietario p = new PessoaProprietario();
        if (d.containsKey("idPessoa")) p.setIdPessoa(integer(d, "idPessoa"));
        if (d.containsKey("idPessoaProprietario")) p.setIdPessoaProprietario(integer(d, "idPessoaProprietario"));
        p.setNome(text(d, "nome"));
        p.setCpf(text(d, "cpf"));
        p.setRg(text(d, "rg"));
        p.setEmail(text(d, "email"));
        p.setTelefone(text(d, "telefone"));
        p.setEndereco(endereco(d));
        p.setObservacoes(text(d, "observacoes"));
        p.setAtivo(!d.containsKey("ativo") || bool(d, "ativo"));
        p.setAceitaContato(!d.containsKey("aceitaContato") || bool(d, "aceitaContato"));
        p.setDataCadastro(date(d, "dataCadastro"));
        return p;
    }

    public static PessoaCorretor corretor(Map<String, Object> d) {
        PessoaCorretor p = new PessoaCorretor();
        if (d.containsKey("idPessoa")) p.setIdPessoa(integer(d, "idPessoa"));
        if (d.containsKey("idPessoaCorretor")) p.setIdPessoaCorretor(integer(d, "idPessoaCorretor"));
        p.setNome(text(d, "nome"));
        p.setCpf(text(d, "cpf"));
        p.setRg(text(d, "rg"));
        p.setEmail(text(d, "email"));
        p.setTelefone(text(d, "telefone"));
        p.setEndereco(endereco(d));
        p.setObservacoes(text(d, "observacoes"));
        p.setAtivo(!d.containsKey("ativo") || bool(d, "ativo"));
        p.setCreci(text(d, "creci"));
        p.setComissaoPercentual(decimal(d, "comissaoPercentual"));
        p.setDataAdmissao(date(d, "dataAdmissao"));
        p.setDataDesligamento(date(d, "dataDesligamento"));
        p.setEspecialidade(text(d, "especialidade"));
        p.setMetaMensal(decimal(d, "metaMensal"));
        return p;
    }

    public static Imovel imovel(Map<String, Object> d) {
        Imovel i = new Imovel();
        if (d.containsKey("idImovel")) i.setIdImovel(integer(d, "idImovel"));
        i.setCodigoImovel(text(d, "codigoImovel"));
        i.setDescricao(text(d, "descricao"));
        i.setTipo(text(d, "tipo"));
        i.setFinalidade(text(d, "finalidade"));
        i.setValorVenda(decimal(d, "valorVenda"));
        i.setValorAluguel(decimal(d, "valorAluguel"));
        i.setValorCondominio(decimal(d, "valorCondominio"));
        i.setValorIptu(decimal(d, "valorIptu"));
        i.setAreaTotal(decimal(d, "areaTotal"));
        i.setAreaConstruida(decimal(d, "areaConstruida"));
        i.setQuartos(integer(d, "quartos"));
        i.setSuites(integer(d, "suites"));
        i.setBanheiros(integer(d, "banheiros"));
        i.setVagasGaragem(integer(d, "vagasGaragem"));
        i.setAndar(integer(d, "andar"));
        i.setAceitaPermuta(bool(d, "aceitaPermuta"));
        i.setAceitaFinanciamento(!d.containsKey("aceitaFinanciamento") || bool(d, "aceitaFinanciamento"));
        i.setMobiliado(bool(d, "mobiliado"));
        i.setStatus(text(d, "status"));
        i.setDestaque(bool(d, "destaque"));

        if (d.get("proprietarioId") != null) {
            PessoaProprietario p = new PessoaProprietario();
            p.setIdPessoaProprietario(integer(d, "proprietarioId"));
            i.setProprietario(p);
        }
        if (d.get("corretorId") != null && !String.valueOf(d.get("corretorId")).isBlank()) {
            PessoaCorretor c = new PessoaCorretor();
            c.setIdPessoaCorretor(integer(d, "corretorId"));
            i.setCorretorResponsavel(c);
        }
        i.setEndereco(endereco(d));
        // A tela de imóveis não possui campo CEP. A tabela Endereco exige
        // um valor NOT NULL, então o cadastro de imóvel usa vazio quando
        // o CEP não é informado pela tela.
        if (i.getEndereco().getCep() == null) {
            i.getEndereco().setCep("");
        }
        return i;
    }
}
