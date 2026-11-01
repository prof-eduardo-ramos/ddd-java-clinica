package br.com.fiap.clinica.web.dto;

import br.com.fiap.clinica.domain.model.Paciente;

import java.util.UUID;

/** DTO de saída: decide o que a API expõe, sem vazar a entidade. */
public record PacienteResponse(UUID id, String nome, String cpf, String email) {

    public static PacienteResponse de(Paciente paciente) {
        return new PacienteResponse(
                paciente.getId(),
                paciente.getNome(),
                paciente.getCpf().formatado(),
                paciente.getEmail().endereco());
    }
}
