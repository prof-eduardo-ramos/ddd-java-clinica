package br.com.fiap.clinica.domain.model;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

/**
 * Agregado raiz: toda regra sobre a consulta mora AQUI.
 * Associação: uma consulta TEM um paciente e TEM um médico.
 */
public class Consulta {

    public static final LocalTime PRIMEIRO_HORARIO = LocalTime.of(7, 0);
    public static final LocalTime ULTIMO_HORARIO = LocalTime.of(18, 0); // consulta de 1h termina às 19h
    public static final Duration ANTECEDENCIA_MINIMA_AGENDAMENTO = Duration.ofMinutes(30);
    public static final Duration ANTECEDENCIA_MINIMA_CANCELAMENTO = Duration.ofHours(24);

    private final UUID id;
    private final Paciente paciente;
    private final Medico medico;
    private final LocalDateTime dataHora;
    private StatusConsulta status;
    private String motivoCancelamento;

    /**
     * Construtor: toda consulta nasce AGENDADA e já validada.
     * O "agora" vem de fora para que as regras de tempo sejam testáveis.
     */
    public Consulta(Paciente paciente, Medico medico, LocalDateTime dataHora, LocalDateTime agora) {
        if (paciente == null) {
            throw new RegraDeNegocioException("Paciente é obrigatório");
        }
        if (medico == null) {
            throw new RegraDeNegocioException("Médico é obrigatório");
        }
        if (dataHora == null || agora == null) {
            throw new RegraDeNegocioException("Data e hora são obrigatórias");
        }
        validarAntecedencia(dataHora, agora);        // Regra 1
        validarHorarioDeFuncionamento(dataHora);     // Regra 2

        this.id = UUID.randomUUID();
        this.paciente = paciente;
        this.medico = medico;
        this.dataHora = dataHora;
        this.status = StatusConsulta.AGENDADA;
    }

    // Regra 4: cancelamento exige motivo e 24h de antecedência
    public void cancelar(String motivo, LocalDateTime agora) {
        if (status != StatusConsulta.AGENDADA) {
            throw new RegraDeNegocioException("Só é possível cancelar uma consulta agendada");
        }
        if (motivo == null || motivo.isBlank()) {
            throw new RegraDeNegocioException("Motivo do cancelamento é obrigatório");
        }
        if (agora.plus(ANTECEDENCIA_MINIMA_CANCELAMENTO).isAfter(dataHora)) {
            throw new RegraDeNegocioException("Cancelamento exige 24 horas de antecedência");
        }
        this.status = StatusConsulta.CANCELADA;
        this.motivoCancelamento = motivo.trim();
    }

    // Regra 5: só consulta agendada pode ser concluída
    public void concluir() {
        if (status != StatusConsulta.AGENDADA) {
            throw new RegraDeNegocioException("Só é possível concluir uma consulta agendada");
        }
        this.status = StatusConsulta.REALIZADA;
    }

    public boolean estaAgendada() {
        return status == StatusConsulta.AGENDADA;
    }

    private static void validarAntecedencia(LocalDateTime dataHora, LocalDateTime agora) {
        if (agora.plus(ANTECEDENCIA_MINIMA_AGENDAMENTO).isAfter(dataHora)) {
            throw new RegraDeNegocioException("Consulta deve ser agendada com 30 minutos de antecedência");
        }
    }

    private static void validarHorarioDeFuncionamento(LocalDateTime dataHora) {
        if (dataHora.getDayOfWeek() == DayOfWeek.SUNDAY) {
            throw new RegraDeNegocioException("A clínica não funciona aos domingos");
        }
        LocalTime horario = dataHora.toLocalTime();
        if (horario.isBefore(PRIMEIRO_HORARIO) || horario.isAfter(ULTIMO_HORARIO)) {
            throw new RegraDeNegocioException("Consultas acontecem entre 7h e 19h (último horário: 18h)");
        }
        if (horario.getMinute() != 0 || horario.getSecond() != 0) {
            throw new RegraDeNegocioException("Consultas começam sempre em hora cheia (ex.: 09:00)");
        }
    }

    public UUID getId() {
        return id;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public Medico getMedico() {
        return medico;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public StatusConsulta getStatus() {
        return status;
    }

    public String getMotivoCancelamento() {
        return motivoCancelamento;
    }
}
