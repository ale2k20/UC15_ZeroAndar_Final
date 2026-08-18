/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 * UC15 - Projeto Integrador 3 - Etapa 8
 *
 * @author Alex
 * @since 15 de agosto de 2026
 * @version 1.8
 */

package uc15.etapa8.zeroandar.test;

import org.junit.jupiter.api.Test;
import uc15.etapa8.zeroandar.model.Imovel;
import uc15.etapa8.zeroandar.model.PessoaCorretor;
import uc15.etapa8.zeroandar.service.ValidacaoService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Testes unitarios da Etapa 8.
 *
 * Os testes utilizam somente regras que nao dependem do banco de dados.
 * O objetivo e verificar automaticamente funcionalidades simples que
 * ja estavam presentes no projeto da Etapa 6.
 */
class ZeroAndarTest {

    /**
     * Testa o calculo simples do valor total de um imovel.
     */
    @Test
    void deveCalcularValorTotalDoImovel() {
        Imovel imovel = new Imovel();
        imovel.setValorVenda(350000.00);
        imovel.setValorIptu(2500.00);

        double resultado = imovel.calcularValorTotal();

        assertEquals(352500.00, resultado, 0.001);
    }

    /**
     * Testa o calculo do valor total quando os valores sao zero.
     */
    @Test
    void deveCalcularValorTotalQuandoValoresForemZero() {
        Imovel imovel = new Imovel();

        assertEquals(0.00, imovel.calcularValorTotal(), 0.001);
    }

    /**
     * Testa o calculo de comissao ja existente na classe PessoaCorretor.
     */
    @Test
    void deveCalcularComissaoDoCorretor() {
        PessoaCorretor corretor = new PessoaCorretor();
        corretor.setComissaoPercentual(5.0);

        double resultado = corretor.calcularComissao(200000.00);

        assertEquals(10000.00, resultado, 0.001);
    }

    /**
     * Testa uma validacao de CPF valida.
     */
    @Test
    void deveAceitarCpfValido() {
        ValidacaoService validacao = new ValidacaoService();

        assertTrue(validacao.validarCpf("529.982.247-25"));
    }

    /**
     * Testa a rejeicao de CPF com sequencia invalida.
     */
    @Test
    void deveRejeitarCpfInvalido() {
        ValidacaoService validacao = new ValidacaoService();

        assertFalse(validacao.validarCpf("111.111.111-11"));
    }

    /**
     * Testa formato de e-mail.
     */
    @Test
    void deveAceitarEmailValido() {
        ValidacaoService validacao = new ValidacaoService();

        assertTrue(validacao.validarEmail("alex@zeroandar.com.br"));
    }

    /**
     * Testa formato de CEP.
     */
    @Test
    void deveAceitarCepValido() {
        ValidacaoService validacao = new ValidacaoService();

        assertTrue(validacao.validarCep("80010-010"));
    }

    /**
     * Testa uma UF brasileira valida.
     */
    @Test
    void deveAceitarUfValida() {
        ValidacaoService validacao = new ValidacaoService();

        assertTrue(validacao.validarEstado("RS"));
    }

    /**
     * Testa nome completo.
     */
    @Test
    void deveAceitarNomeCompleto() {
        ValidacaoService validacao = new ValidacaoService();

        assertTrue(validacao.validarNome("Alex Silva"));
    }

    /**
     * Testa telefone com DDD e onze digitos.
     */
    @Test
    void deveAceitarTelefoneValido() {
        ValidacaoService validacao = new ValidacaoService();

        assertTrue(validacao.validarTelefone("(51) 99999-9999"));
    }
}
