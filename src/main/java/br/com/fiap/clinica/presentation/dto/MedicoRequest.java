package br.com.fiap.clinica.presentation.dto;

import br.com.fiap.clinica.domain.model.Crm;
import br.com.fiap.clinica.domain.model.Especialidade;
import br.com.fiap.clinica.domain.model.Medico;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/** Dados do médico que chegam no JSON. */
public record MedicoRequest(
        @NotBlank String nome,
        @NotBlank String crm,
        @NotBlank String uf,
        @NotNull Especialidade especialidade) {

    public Medico paraMedico() {
        return new Medico(nome, new Crm(crm, uf), especialidade);
    }
}
