package br.com.fiap.clinica.presentation.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/** DTO de entrada: só formato. As regras de negócio continuam no domínio. */
public record AgendarConsultaRequest(
        @NotNull @Valid PacienteRequest paciente,
        @NotNull @Valid MedicoRequest medico,
        @NotNull LocalDateTime dataHora) {
}
