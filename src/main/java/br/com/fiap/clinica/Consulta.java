package br.com.fiap.clinica;

import java.time.LocalDateTime;
import java.util.UUID;

public class Consulta {

    private UUID id = UUID.randomUUID();
    private Paciente paciente;
    private Medico medico;
    private LocalDateTime dataHora;
    private StatusConsulta status;
    private String motivoCancelamento;

    // Sem setters: o status de uma consulta só pode mudar seguindo as regras
    // do negócio (agendar, cancelar, concluir). Isso entra no Passo 3.

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
