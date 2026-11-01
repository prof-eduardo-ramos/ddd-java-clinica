package br.com.fiap.clinica.application;

import br.com.fiap.clinica.domain.exception.RegraDeNegocioException;
import br.com.fiap.clinica.domain.model.Consulta;
import br.com.fiap.clinica.domain.model.Especialidade;
import br.com.fiap.clinica.domain.model.Medico;
import br.com.fiap.clinica.domain.model.Paciente;
import br.com.fiap.clinica.infrastructure.persistence.ConsultaRepositoryEmMemoria;
import br.com.fiap.clinica.infrastructure.persistence.MedicoRepositoryEmMemoria;
import br.com.fiap.clinica.infrastructure.persistence.PacienteRepositoryEmMemoria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** Teste do caso de uso SEM Spring: só Java, repositórios em memória e um relógio fixo. */
class ConsultaServiceTest {

    private static final ZoneId FUSO = ZoneId.of("America/Sao_Paulo");
    private static final LocalDateTime AGORA = LocalDateTime.of(2026, 11, 2, 9, 0); // segunda-feira
    private static final LocalDateTime AMANHA_10H = AGORA.plusDays(1).withHour(10);

    private ConsultaService consultaService;
    private Paciente paciente;
    private Medico medico;

    @BeforeEach
    void configurar() {
        var pacientes = new PacienteRepositoryEmMemoria();
        var medicos = new MedicoRepositoryEmMemoria();
        var relogioFixo = Clock.fixed(AGORA.atZone(FUSO).toInstant(), FUSO);

        consultaService = new ConsultaService(new ConsultaRepositoryEmMemoria(), pacientes, medicos, relogioFixo);
        paciente = new PacienteService(pacientes).cadastrar("Ana Souza", "529.982.247-25", "ana@email.com");
        medico = new MedicoService(medicos).cadastrar("Carlos Lima", "123456", "SP", Especialidade.CARDIOLOGIA);
    }

    @Test
    @DisplayName("Agenda consulta para paciente e médico existentes")
    void agendaConsulta() {
        Consulta consulta = consultaService.agendar(paciente.getId(), medico.getId(), AMANHA_10H);
        assertEquals(consulta, consultaService.buscar(consulta.getId()));
    }

    @Test
    @DisplayName("Regra 3: médico não pode ter duas consultas no mesmo horário")
    void naoPermiteConflitoDeHorario() {
        consultaService.agendar(paciente.getId(), medico.getId(), AMANHA_10H);
        assertThrows(RegraDeNegocioException.class,
                () -> consultaService.agendar(paciente.getId(), medico.getId(), AMANHA_10H));
    }

    @Test
    @DisplayName("Regra 3: horário de consulta cancelada fica livre de novo")
    void horarioCanceladoFicaLivre() {
        var segundaQueVem = AGORA.plusDays(7).withHour(10);
        Consulta consulta = consultaService.agendar(paciente.getId(), medico.getId(), segundaQueVem);
        consultaService.cancelar(consulta.getId(), "Paciente viajou");
        Consulta nova = consultaService.agendar(paciente.getId(), medico.getId(), segundaQueVem);
        assertEquals(segundaQueVem, nova.getDataHora());
    }

    @Test
    @DisplayName("Paciente inexistente gera RecursoNaoEncontradoException")
    void pacienteInexistente() {
        assertThrows(RecursoNaoEncontradoException.class,
                () -> consultaService.agendar(UUID.randomUUID(), medico.getId(), AMANHA_10H));
    }
}
