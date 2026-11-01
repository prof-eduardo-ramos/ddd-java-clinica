package br.com.fiap.clinica.web.dto;

import jakarta.validation.constraints.NotBlank;

/** DTO de entrada: só formato. As regras de negócio continuam no domínio. */
public record CadastrarPacienteRequest(
        @NotBlank String nome,
        @NotBlank String cpf,
        @NotBlank String email) {
}
