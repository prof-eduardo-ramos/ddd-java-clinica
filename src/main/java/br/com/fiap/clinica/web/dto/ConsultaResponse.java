package br.com.fiap.clinica.web.dto;

import br.com.fiap.clinica.domain.model.Consulta;
import br.com.fiap.clinica.domain.model.StatusConsulta;

import java.time.LocalDateTime;
import java.util.UUID;

public record ConsultaResponse(UUID id, UUID pacienteId, UUID medicoId, LocalDateTime dataHora,
                               StatusConsulta status, String motivoCancelamento) {

    public static ConsultaResponse de(Consulta consulta) {
        return new ConsultaResponse(consulta.getId(), consulta.getPacienteId(), consulta.getMedicoId(),
                consulta.getDataHora(), consulta.getStatus(), consulta.getMotivoCancelamento());
    }
}
