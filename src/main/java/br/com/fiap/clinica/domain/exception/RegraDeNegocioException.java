package br.com.fiap.clinica.domain.exception;

/**
 * Erro lançado quando uma regra de negócio do domínio é violada.
 * Ex.: agendar consulta no domingo, CPF inválido, cancelar sem motivo.
 */
public class RegraDeNegocioException extends RuntimeException {

    public RegraDeNegocioException(String mensagem) {
        super(mensagem);
    }
}
