package br.com.fiap.clinica.domain.model;

import br.com.fiap.clinica.domain.exception.RegraDeNegocioException;

/** Objeto de valor que representa um e-mail válido, sempre em minúsculas. */
public record Email(String endereco) {

    private static final String FORMATO = "^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$";

    public Email {
        if (endereco == null || endereco.isBlank()) {
            throw new RegraDeNegocioException("E-mail é obrigatório");
        }
        endereco = endereco.trim().toLowerCase();
        if (!endereco.matches(FORMATO)) {
            throw new RegraDeNegocioException("E-mail inválido: " + endereco);
        }
    }
}
