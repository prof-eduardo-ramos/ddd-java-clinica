package br.com.fiap.clinica.domain.model;

/**
 * Objeto de valor: não tem identidade própria, é definido pelo seu valor.
 * Dois CPFs com o mesmo número são iguais (o record gera equals/hashCode).
 * Se o objeto existe, ele é válido: a validação acontece no construtor.
 */
public record Cpf(String numero) {

    public Cpf {
        if (numero == null || numero.isBlank()) {
            throw new RegraDeNegocioException("CPF é obrigatório");
        }
        numero = numero.replaceAll("\\D", ""); // remove pontos e traço
        if (!numero.matches("\\d{11}") || todosDigitosIguais(numero) || !digitosVerificadoresValidos(numero)) {
            throw new RegraDeNegocioException("CPF inválido: " + numero);
        }
    }

    /** Retorna o CPF no formato 000.000.000-00. */
    public String formatado() {
        return numero.substring(0, 3) + "." + numero.substring(3, 6) + "."
                + numero.substring(6, 9) + "-" + numero.substring(9);
    }

    private static boolean todosDigitosIguais(String cpf) {
        return cpf.chars().distinct().count() == 1;
    }

    private static boolean digitosVerificadoresValidos(String cpf) {
        return calcularDigito(cpf, 9) == Character.getNumericValue(cpf.charAt(9))
                && calcularDigito(cpf, 10) == Character.getNumericValue(cpf.charAt(10));
    }

    private static int calcularDigito(String cpf, int quantidadeDigitos) {
        int soma = 0;
        int peso = quantidadeDigitos + 1;
        for (int i = 0; i < quantidadeDigitos; i++) {
            soma += Character.getNumericValue(cpf.charAt(i)) * peso--;
        }
        int resto = soma % 11;
        return resto < 2 ? 0 : 11 - resto;
    }
}
