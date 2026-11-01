package br.com.fiap.clinica.presentation.dto;

import br.com.fiap.clinica.domain.model.Cpf;
import br.com.fiap.clinica.domain.model.Email;
import br.com.fiap.clinica.domain.model.Paciente;
import jakarta.validation.constraints.NotBlank;

/** Dados do paciente que chegam no JSON. */
public record PacienteRequest(
        @NotBlank String nome,
        @NotBlank String cpf,
        @NotBlank String email) {

    // Converte o DTO no objeto de domínio: as invariantes são validadas aqui
    public Paciente paraPaciente() {
        return new Paciente(nome, new Cpf(cpf), new Email(email));
    }
}
