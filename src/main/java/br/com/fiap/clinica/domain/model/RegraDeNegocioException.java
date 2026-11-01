package br.com.fiap.clinica.domain.model;

/**
 * Lançada quando uma invariante de negócio é violada.
 * Ex.: CPF inválido, nome vazio, consulta marcada para domingo.
 */
public class RegraDeNegocioException extends RuntimeException {

    public RegraDeNegocioException(String mensagem) {
        super(mensagem);
    }
}
