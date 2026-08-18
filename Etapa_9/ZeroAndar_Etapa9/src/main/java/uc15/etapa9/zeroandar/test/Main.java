/**
 * UC15 - Projeto Integrador 3 - Etapa 9
 *
 * @author Alex
 * @since 15 de agosto de 2026
 * @version 1.9
 */
package uc15.etapa9.zeroandar.test;

import java.util.ArrayList;
import java.util.List;
import uc15.etapa9.zeroandar.model.Endereco;
import uc15.etapa9.zeroandar.model.Imovel;
import uc15.etapa9.zeroandar.model.PessoaCliente;
import uc15.etapa9.zeroandar.model.PessoaProprietario;
import uc15.etapa9.zeroandar.repository.ClienteDAO;
import uc15.etapa9.zeroandar.service.ClienteService;
import uc15.etapa9.zeroandar.service.ValidacaoService;

/**
 * Testes demonstrativos da Etapa 6 executados pelo metodo main().
 */
public final class Main {

    private static int total;
    private static int aprovados;

    private Main() {
    }

    public static void main(String[] args) {
        System.out.println("=======================================================");
        System.out.println("    ZeroAndar - PI3 Etapa 6 - Testes de Refatoracao");
        System.out.println("=======================================================\n");
        testarValidacao();
        testarModelos();
        testarInversaoDeDependencia();
        System.out.printf("\nRESULTADO: %d/%d testes passaram.%n", aprovados, total);
        if (aprovados != total) {
            System.exit(1);
        }
    }

    private static void testarValidacao() {
        ValidacaoService v = new ValidacaoService();
        verificar("CPF valido", v.validarCpf("529.982.247-25"));
        verificar("CPF invalido", !v.validarCpf("111.111.111-11"));
        verificar("E-mail valido", v.validarEmail("alex@zeroandar.com.br"));
        verificar("CEP valido", v.validarCep("80010-010"));
        verificar("UF valida", v.validarEstado("RS"));
        verificar("Nome completo", v.validarNome("Alex Silva"));
        verificar("Telefone valido", v.validarTelefone("(51) 99999-9999"));
    }

    private static void testarModelos() {
        PessoaCliente cliente = new PessoaCliente("Maria Oliveira", "529.982.247-25", "maria@email.com");
        verificar("Heranca Pessoa -> PessoaCliente", "Maria Oliveira".equals(cliente.getNome()));
        verificar("Cliente ativo por padrao", cliente.isAtivo());
        Endereco endereco = new Endereco("Rua das Flores", "100", "Centro", "Porto Alegre", "RS", "90010-010");
        verificar("Endereco formatado", endereco.getEnderecoCompleto().contains("Porto Alegre/RS"));
        PessoaProprietario proprietario = new PessoaProprietario();
        proprietario.setNome("Carlos Souza");
        Imovel imovel = new Imovel("ZA-001", "Apartamento", proprietario);
        imovel.setValorVenda(350000);
        verificar("Imovel disponivel por padrao", "disponivel".equals(imovel.getStatus()));
        verificar("Imovel aceita financiamento por padrao", imovel.isAceitaFinanciamento());
    }

    private static void testarInversaoDeDependencia() {
        ClienteDAO memoria = new ClienteDAOEmMemoria();
        ClienteService service = new ClienteService(memoria);
        PessoaCliente cliente = new PessoaCliente("Joao Silva", "529.982.247-25", "joao@email.com");
        verificar("Service aceita repositorio por abstracao", service.adicionar(cliente));
        verificar("Service consulta repositorio por abstracao", service.buscarPorId(cliente.getIdPessoaCliente()) != null);
        verificar("Service lista sem depender de Swing", service.listarTodos().size() == 1);
    }

    private static void verificar(String descricao, boolean resultado) {
        total++;
        if (resultado) {
            aprovados++;
            System.out.println("[OK] " + descricao);
        } else {
            System.out.println("[FALHOU] " + descricao);
        }
    }

    /**
     * Fake repository local para demonstrar DIP e permitir teste sem MySQL.
     */
    private static final class ClienteDAOEmMemoria implements ClienteDAO {

        private final List<PessoaCliente> dados = new ArrayList<>();
        private int proximoId = 1;

        public int inserir(PessoaCliente c) {
            c.setIdPessoaCliente(proximoId++);
            dados.add(c);
            return c.getIdPessoaCliente();
        }

        public boolean atualizar(PessoaCliente c) {
            return dados.stream()
                    .anyMatch(x -> x.getIdPessoaCliente() == c.getIdPessoaCliente());
        }

        public boolean excluir(int id) {
            return dados.removeIf(x -> x.getIdPessoaCliente() == id);
        }

        public PessoaCliente buscarPorId(int id) {
            return dados.stream()
                    .filter(x -> x.getIdPessoaCliente() == id)
                    .findFirst()
                    .orElse(null);
        }

        public PessoaCliente buscarPorCpf(String cpf) {
            return dados.stream().filter(x -> cpf != null && cpf.equals(x.getCpf())).findFirst().orElse(null);
        }

        public List<PessoaCliente> listarTodos() {
            return new ArrayList<>(dados);
        }

        public List<PessoaCliente> listarAtivos() {
            return dados.stream()
                    .filter(PessoaCliente::isAtivo)
                    .toList();
        }

        public List<PessoaCliente> buscarPorNome(String nome) {
            return dados.stream()
                    .filter(x -> x.getNome().toLowerCase().contains(nome.toLowerCase()))
                    .toList();
        }

        public int contarTotal() {
            return dados.size();
        }

        public int contarAtivos() {
            return (int) dados
                    .stream().filter(PessoaCliente::isAtivo)
                    .count();
        }
    }
}
