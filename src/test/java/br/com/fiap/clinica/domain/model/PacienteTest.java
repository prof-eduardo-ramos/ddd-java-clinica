package br.com.fiap.clinica.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    @DisplayName("Construtor rejeita nome com menos de 3 caracteres")
    void nomeCurto() {
        assertThrows(RegraDeNegocioException.class, () -> new Paciente("Al", cpf, email));
    }

    @Test
    @DisplayName("Construtor rejeita CPF ausente")
    void cpfObrigatorio() {
        assertThrows(RegraDeNegocioException.class, () -> new Paciente("Ana Souza", null, email));
    }

    @Test
    @DisplayName("Setter também protege a invariante do nome")
    void setterProtegeNome() {
        Paciente paciente = new Paciente("Ana Souza", cpf, email);
        assertThrows(RegraDeNegocioException.class, () -> paciente.setNome(""));
        assertEquals("Ana Souza", paciente.getNome());
    }

    @Test
    @DisplayName("E-mail pode ser alterado por um valor válido")
    void alteraEmail() {
        Paciente paciente = new Paciente("Ana Souza", cpf, email);
        paciente.setEmail(new Email("ana.souza@email.com"));
        assertEquals("ana.souza@email.com", paciente.getEmail().endereco());
    }
}
