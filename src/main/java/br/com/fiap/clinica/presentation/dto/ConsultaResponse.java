package br.com.fiap.clinica.presentation.dto;

import br.com.fiap.clinica.domain.model.Consulta;
import br.com.fiap.clinica.domain.model.Especialidade;
import br.com.fiap.clinica.domain.model.StatusConsulta;

import java.time.LocalDateTime;
import java.util.UUID;

/** DTO de saída: decide o que a API expõe, sem devolver as entidades diretamente. */
public record ConsultaResponse(UUID id, String paciente, String medico, Especialidade especialidade,
                               LocalDateTime dataHora, StatusConsulta status, String motivoCancelamento) {

    public static ConsultaResponse de(Consulta consulta) {
        return new ConsultaResponse(
                consulta.getId(),
                consulta.getPaciente().getNome(),
                consulta.getMedico().getNome(),
                consulta.getMedico().getEspecialidade(),
                consulta.getDataHora(),
                consulta.getStatus(),
                consulta.getMotivoCancelamento());
    }
}
