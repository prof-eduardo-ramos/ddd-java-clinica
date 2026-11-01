package br.com.fiap.clinica.web.dto;

import br.com.fiap.clinica.domain.model.Especialidade;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CadastrarMedicoRequest(
        @NotBlank String nome,
        @NotBlank String crm,
        @NotBlank String uf,
        @NotNull Especialidade especialidade) {
}
