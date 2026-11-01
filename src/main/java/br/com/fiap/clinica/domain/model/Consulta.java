package br.com.fiap.clinica.domain.model;

import br.com.fiap.clinica.domain.exception.RegraDeNegocioException;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Agregado raiz: é a porta de entrada para alterar uma consulta.
 * Toda regra sobre a consulta mora AQUI, e não no controller ou no service.
 *
 * Paciente e Médico são referenciados apenas pelo id: um agregado
 * não carrega outro agregado dentro dele.
 */
public class Consulta {

    public static final LocalTime PRIMEIRO_HORARIO = LocalTime.of(7, 0);
    public static final LocalTime ULTIMO_HORARIO = LocalTime.of(18, 0); // consulta de 1h termina às 19h
    public static final Duration ANTECEDENCIA_MINIMA_AGENDAMENTO = Duration.ofMinutes(30);
    public static final Duration ANTECEDENCIA_MINIMA_CANCELAMENTO = Duration.ofHours(24);

    private final UUID id;
    private final UUID pacienteId;
    private final UUID medicoId;
    private final LocalDateTime dataHora;
    private StatusConsulta status;
    private String motivoCancelamento;

    // Construtor privado: a única forma de criar uma consulta é pelo método agendar()
    private Consulta(UUID pacienteId, UUID medicoId, LocalDateTime dataHora) {
        this.id = UUID.randomUUID();
        this.pacienteId = pacienteId;
        this.medicoId = medicoId;
        this.dataHora = dataHora;
        this.status = StatusConsulta.AGENDADA;
    }

    /**
     * Fábrica estática: cria a consulta já validada.
     * O "agora" vem de fora para que as regras de tempo sejam testáveis.
     */
    public static Consulta agendar(UUID pacienteId, UUID medicoId, LocalDateTime dataHora, LocalDateTime agora) {
        Objects.requireNonNull(pacienteId, "Paciente é obrigatório");
        Objects.requireNonNull(medicoId, "Médico é obrigatório");
        Objects.requireNonNull(dataHora, "Data e hora são obrigatórias");
        Objects.requireNonNull(agora, "Data e hora atual são obrigatórias");

        validarAntecedencia(dataHora, agora);
        validarHorarioDeFuncionamento(dataHora);

        return new Consulta(pacienteId, medicoId, dataHora);
    }

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

    public void concluir() {
        if (status != StatusConsulta.AGENDADA) {
            throw new RegraDeNegocioException("Só é possível concluir uma consulta agendada");
        }
        this.status = StatusConsulta.REALIZADA;
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

    public boolean estaAgendada() {
        return status == StatusConsulta.AGENDADA;
    }

    public UUID getId() { return id; }
    public UUID getPacienteId() { return pacienteId; }
    public UUID getMedicoId() { return medicoId; }
    public LocalDateTime getDataHora() { return dataHora; }
    public StatusConsulta getStatus() { return status; }
    public String getMotivoCancelamento() { return motivoCancelamento; }

    @Override
    public boolean equals(Object o) {
        return o instanceof Consulta outra && id.equals(outra.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
