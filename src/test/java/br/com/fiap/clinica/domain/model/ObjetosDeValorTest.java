package br.com.fiap.clinica.domain.model;

import br.com.fiap.clinica.domain.exception.RegraDeNegocioException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ObjetosDeValorTest {

    @Test
    @DisplayName("CPF válido é aceito e guardado só com números")
    void cpfValido() {
        Cpf cpf = new Cpf("529.982.247-25");
        assertEquals("52998224725", cpf.numero());
        assertEquals("529.982.247-25", cpf.formatado());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "123", "111.111.111-11", "529.982.247-26", "abc.def.ghi-jk"})
    @DisplayName("CPF inválido lança RegraDeNegocioException")
    void cpfInvalido(String numero) {
        assertThrows(RegraDeNegocioException.class, () -> new Cpf(numero));
    }

    @Test
    @DisplayName("Dois CPFs com o mesmo número são iguais (igualdade por valor)")
    void cpfIgualdadePorValor() {
        assertEquals(new Cpf("52998224725"), new Cpf("529.982.247-25"));
    }

    @Test
    @DisplayName("E-mail é normalizado para minúsculas")
    void emailValido() {
        assertEquals("ana@email.com", new Email("  Ana@Email.COM ").endereco());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "ana", "ana@", "ana@email", "@email.com"})
    @DisplayName("E-mail inválido lança RegraDeNegocioException")
    void emailInvalido(String endereco) {
        assertThrows(RegraDeNegocioException.class, () -> new Email(endereco));
    }

    @Test
    @DisplayName("CRM válido tem UF em maiúsculas")
    void crmValido() {
        Crm crm = new Crm("123456", "sp");
        assertEquals("SP", crm.uf());
        assertEquals("CRM/SP 123456", crm.toString());
    }

    @Test
    @DisplayName("CRM com número ou UF inválidos é rejeitado")
    void crmInvalido() {
        assertThrows(RegraDeNegocioException.class, () -> new Crm("12", "SP"));
        assertThrows(RegraDeNegocioException.class, () -> new Crm("123456", "São Paulo"));
    }
}
