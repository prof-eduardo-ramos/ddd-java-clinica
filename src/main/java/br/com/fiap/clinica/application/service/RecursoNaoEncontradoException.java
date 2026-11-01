package br.com.fiap.clinica.application.service;

/** Lançada quando a consulta procurada não existe. */
public class RecursoNaoEncontradoException extends RuntimeException {

    public RecursoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
