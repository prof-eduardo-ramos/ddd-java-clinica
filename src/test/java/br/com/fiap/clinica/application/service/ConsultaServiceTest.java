package br.com.fiap.clinica.application.service;

import br.com.fiap.clinica.domain.model.Consulta;
import br.com.fiap.clinica.domain.model.Cpf;
import br.com.fiap.clinica.domain.model.Crm;
import br.com.fiap.clinica.domain.model.Email;
import br.com.fiap.clinica.domain.model.Especialidade;
import br.com.fiap.clinica.domain.model.Medico;
import br.com.fiap.clinica.domain.model.Paciente;
import br.com.fiap.clinica.domain.model.RegraDeNegocioException;
import br.com.fiap.clinica.domain.model.StatusConsulta;
import br.com.fiap.clinica.infrastructure.repository.ConsultaRepositoryEmMemoria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** Teste do caso de uso SEM Spring: só Java, o adaptador em memória e um relógio fixo. */
class ConsultaServiceTest {

    private static final ZoneId FUSO = ZoneId.of("America/Sao_Paulo");
    private static final LocalDateTime AGORA = LocalDateTime.of(2026, 11, 2, 9, 0); // segunda-feira
    private static final LocalDateTime AMANHA_10H = AGORA.plusDays(1).withHour(10);

    private ConsultaService service;
    private final Paciente ana = new Paciente("Ana Souza", new Cpf("529.982.247-25"), new Email("ana@email.com"));
    private final Paciente bruno = new Paciente("Bruno Alves", new Cpf("153.509.460-56"), new Email("bruno@email.com"));
    private final Medico carlos = new Medico("Carlos Lima", new Crm("123456", "SP"), Especialidade.CARDIOLOGIA);

    @BeforeEach
    void configurar() {
        Clock relogioFixo = Clock.fixed(AGORA.atZone(FUSO).toInstant(), FUSO);
        service = new ConsultaService(new ConsultaRepositoryEmMemoria(), relogioFixo);
    }

    @Test
    @DisplayName("Agenda e encontra a consulta pelo id")
    void agendaConsulta() {
        Consulta consulta = service.agendar(ana, carlos, AMANHA_10H);
        assertEquals(consulta, service.buscar(consulta.getId()));
    }

    @Test
    @DisplayName("Regra 3: médico não pode ter duas consultas no mesmo horário")
    void naoPermiteConflitoDeHorario() {
        service.agendar(ana, carlos, AMANHA_10H);
        assertThrows(RegraDeNegocioException.class,
                () -> service.agendar(bruno, carlos, AMANHA_10H));
    }

    @Test
    @DisplayName("Regra 3: horário de consulta cancelada fica livre de novo")
    void horarioCanceladoFicaLivre() {
        LocalDateTime semanaQueVem = AGORA.plusDays(7).withHour(10);
        Consulta consulta = service.agendar(ana, carlos, semanaQueVem);
        service.cancelar(consulta.getId(), "Paciente viajou");
        assertEquals(StatusConsulta.CANCELADA, service.buscar(consulta.getId()).getStatus());
        assertEquals(semanaQueVem, service.agendar(bruno, carlos, semanaQueVem).getDataHora());
    }

    @Test
    @DisplayName("Consulta inexistente gera RecursoNaoEncontradoException")
    void consultaInexistente() {
        assertThrows(RecursoNaoEncontradoException.class, () -> service.buscar(UUID.randomUUID()));
    }
}
