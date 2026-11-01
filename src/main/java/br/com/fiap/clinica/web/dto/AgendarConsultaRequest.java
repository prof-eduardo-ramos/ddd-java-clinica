package br.com.fiap.clinica.web.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record AgendarConsultaRequest(
        @NotNull UUID pacienteId,
        @NotNull UUID medicoId,
        @NotNull LocalDateTime dataHora) {
}
