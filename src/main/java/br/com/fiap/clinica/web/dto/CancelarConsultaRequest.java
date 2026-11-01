package br.com.fiap.clinica.web.dto;

import jakarta.validation.constraints.NotBlank;

public record CancelarConsultaRequest(@NotBlank String motivo) {
}
