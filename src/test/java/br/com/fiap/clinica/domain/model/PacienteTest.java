package br.com.fiap.clinica.domain.model;

import br.com.fiap.clinica.domain.exception.RegraDeNegocioException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PacienteTest {

    private final Cpf cpf = new Cpf("529.982.247-25");
    private final Email email = new Email("ana@email.com");

    @Test
    @DisplayName("Paciente nasce com identidade própria")
    void criaPacienteComId() {
        Paciente paciente = new Paciente("Ana Souza", cpf, email);
        assertNotNull(paciente.getId());
        assertEquals("Ana Souza", paciente.getNome());
    }

    @Test
    @DisplayName("Nome com menos de 3 caracteres é rejeitado")
    void nomeCurto() {
        assertThrows(RegraDeNegocioException.class, () -> new Paciente("Al", cpf, email));
    }

    @Test
    @DisplayName("Entidades com os mesmos dados mas ids diferentes NÃO são iguais")
    void igualdadePorIdentidade() {
        assertNotEquals(new Paciente("Ana Souza", cpf, email), new Paciente("Ana Souza", cpf, email));
    }

    @Test
    @DisplayName("Alterar e-mail é um comportamento da entidade")
    void alteraEmail() {
        Paciente paciente = new Paciente("Ana Souza", cpf, email);
        paciente.alterarEmail(new Email("ana.souza@email.com"));
        assertEquals("ana.souza@email.com", paciente.getEmail().endereco());
    }
}
