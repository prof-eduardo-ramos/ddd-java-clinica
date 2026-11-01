package br.com.fiap.clinica.application;

/** Lançada quando um paciente, médico ou consulta não existe. */
public class RecursoNaoEncontradoException extends RuntimeException {

    public RecursoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
