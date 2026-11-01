package br.com.fiap.clinica.presentation.dto;

import jakarta.validation.constraints.NotBlank;

public record CancelarConsultaRequest(@NotBlank String motivo) {
}
