/**
 * UC15 - Projeto Integrador 3 - Etapa 9
 *
 * @author Alex
 * @since 15 de agosto de 2026
 * @version 1.9
 */
package uc15.etapa9.zeroandar.service;

public class ValidacaoService {

    /**
     * Valida CPF brasileiro. Remove formatacao (pontos e traco) antes de
     * validar. Verifica digitos verificadores conforme algoritmo da Receita
     * Federal.
     *
     * @param cpf CPF com ou sem formatacao
     * @return true se o CPF e valido
     */
    public boolean validarCpf(String cpf) {
        if (cpf == null || cpf.isBlank()) {
            return false;
        }

        // Remove formatacao
        String cpfLimpo = cpf.replaceAll("[^0-9]", "");

        if (cpfLimpo.length() != 11) {
            return false;
        }

        // Rejeita CPFs com todos os digitos iguais (ex: 111.111.111-11)
        if (cpfLimpo.matches("(\\d)\\1{10}")) {
            return false;
        }

        // Calcula primeiro digito verificador
        int soma = 0;
        for (int i = 0; i < 9; i++) {
            soma += Character.getNumericValue(cpfLimpo.charAt(i)) * (10 - i);
        }
        int primeiroDigito = (soma % 11 < 2) ? 0 : 11 - (soma % 11);
        if (primeiroDigito != Character.getNumericValue(cpfLimpo.charAt(9))) {
            return false;
        }

        // Calcula segundo digito verificador
        soma = 0;
        for (int i = 0; i < 10; i++) {
            soma += Character.getNumericValue(cpfLimpo.charAt(i)) * (11 - i);
        }
        int segundoDigito = (soma % 11 < 2) ? 0 : 11 - (soma % 11);
        return segundoDigito == Character.getNumericValue(cpfLimpo.charAt(10));
    }

    /**
     * Valida formato de e-mail.
     *
     * @param email endereco de e-mail a validar
     * @return true se o formato e valido
     */
    public boolean validarEmail(String email) {
        if (email == null || email.isBlank()) {
            return false;
        }
        return email.matches("^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$");
    }

    /**
     * Valida CEP brasileiro (formato 00000-000 ou 00000000).
     *
     * @param cep CEP com ou sem traco
     * @return true se o formato e valido
     */
    public boolean validarCep(String cep) {
        if (cep == null || cep.isBlank()) {
            return false;
        }
        String cepLimpo = cep.replace("-", "");
        return cepLimpo.matches("\\d{8}");
    }

    /**
     * Valida sigla de estado brasileiro (UF).
     *
     * @param estado sigla do estado (ex: "SP", "PR")
     * @return true se e uma UF valida
     */
    public boolean validarEstado(String estado) {
        if (estado == null || estado.isBlank()) {
            return false;
        }
        String[] ufsValidas = {
            "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO",
            "MA", "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI",
            "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO"
        };
        for (String uf : ufsValidas) {
            if (uf.equalsIgnoreCase(estado)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Valida nome: nao pode ser vazio e deve ter ao menos duas palavras.
     *
     * @param nome nome completo
     * @return true se o nome parece valido
     */
    public boolean validarNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            return false;
        }
        String[] partes = nome.trim().split("\\s+");
        return partes.length >= 2;
    }

    /**
     * Valida telefone (aceita formatos com ou sem DDD e com ou sem formatacao).
     *
     * @param telefone numero de telefone
     * @return true se o formato e valido
     */
    public boolean validarTelefone(String telefone) {
        if (telefone == null || telefone.isBlank()) {
            return false;
        }
        String telLimpo = telefone.replaceAll("[^0-9]", "");
        // Aceita 10 digitos (fixo com DDD) ou 11 digitos (celular com DDD)
        return telLimpo.length() == 10 || telLimpo.length() == 11;
    }

    /**
     * Valida valor monetario: deve ser positivo.
     *
     * @param valor valor em reais
     * @return true se o valor e valido
     */
    public boolean validarValor(double valor) {
        return valor >= 0;
    }
}
