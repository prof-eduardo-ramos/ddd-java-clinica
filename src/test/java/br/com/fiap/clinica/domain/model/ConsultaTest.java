package br.com.fiap.clinica.domain.model;

import br.com.fiap.clinica.domain.exception.RegraDeNegocioException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ConsultaTest {

    // Segunda-feira, 02/11/2026, 09:00 — o "agora" fixo deixa o teste previsível
    private static final LocalDateTime AGORA = LocalDateTime.of(2026, 11, 2, 9, 0);
    private final UUID pacienteId = UUID.randomUUID();
    private final UUID medicoId = UUID.randomUUID();

    private Consulta agendarPara(LocalDateTime dataHora) {
        return Consulta.agendar(pacienteId, medicoId, dataHora, AGORA);
    }

    @Nested
    class Agendamento {

        @Test
        @DisplayName("Consulta válida nasce com status AGENDADA")
        void agendaConsultaValida() {
            Consulta consulta = agendarPara(AGORA.plusDays(1).withHour(10));
            assertEquals(StatusConsulta.AGENDADA, consulta.getStatus());
        }

        @Test
        @DisplayName("Regra 1: exige 30 minutos de antecedência")
        void exigeAntecedencia() {
            assertThrows(RegraDeNegocioException.class, () -> agendarPara(AGORA.plusMinutes(20)));
        }

        @Test
        @DisplayName("Regra 2: não funciona aos domingos")
        void naoAgendaNoDomingo() {
            LocalDateTime domingo = LocalDateTime.of(2026, 11, 8, 10, 0);
            assertThrows(RegraDeNegocioException.class, () -> agendarPara(domingo));
        }

        @Test
        @DisplayName("Regra 2: só entre 7h e 18h (consulta termina às 19h)")
        void respeitaHorarioDeFuncionamento() {
            assertThrows(RegraDeNegocioException.class, () -> agendarPara(AGORA.plusDays(1).withHour(6)));
            assertThrows(RegraDeNegocioException.class, () -> agendarPara(AGORA.plusDays(1).withHour(19)));
        }

        @Test
        @DisplayName("Regra 2: sempre em hora cheia")
        void exigeHoraCheia() {
            assertThrows(RegraDeNegocioException.class,
                    () -> agendarPara(AGORA.plusDays(1).withHour(10).withMinute(30)));
        }
    }

    @Nested
    class Cancelamento {

        @Test
        @DisplayName("Regra 4: cancela com motivo e 24h de antecedência")
        void cancelaComMotivo() {
            Consulta consulta = agendarPara(AGORA.plusDays(2).withHour(10));
            consulta.cancelar("Paciente viajou", AGORA);
            assertEquals(StatusConsulta.CANCELADA, consulta.getStatus());
            assertEquals("Paciente viajou", consulta.getMotivoCancelamento());
        }

        @Test
        @DisplayName("Regra 4: motivo é obrigatório")
        void exigeMotivo() {
            Consulta consulta = agendarPara(AGORA.plusDays(2).withHour(10));
            assertThrows(RegraDeNegocioException.class, () -> consulta.cancelar(" ", AGORA));
        }

        @Test
        @DisplayName("Regra 4: não cancela com menos de 24h")
        void exige24Horas() {
            Consulta consulta = agendarPara(AGORA.withHour(15));
            assertThrows(RegraDeNegocioException.class, () -> consulta.cancelar("Imprevisto", AGORA));
        }
    }

    @Nested
    class Conclusao {

        @Test
        @DisplayName("Regra 5: consulta agendada pode ser concluída")
        void concluiConsulta() {
            Consulta consulta = agendarPara(AGORA.plusDays(1).withHour(10));
            consulta.concluir();
            assertEquals(StatusConsulta.REALIZADA, consulta.getStatus());
        }

        @Test
        @DisplayName("Regra 5: consulta cancelada não pode ser concluída")
        void naoConcluiCancelada() {
            Consulta consulta = agendarPara(AGORA.plusDays(2).withHour(10));
            consulta.cancelar("Paciente viajou", AGORA);
            assertThrows(RegraDeNegocioException.class, consulta::concluir);
        }
    }
}
