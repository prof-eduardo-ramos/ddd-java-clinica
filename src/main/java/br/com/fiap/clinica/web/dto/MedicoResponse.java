package br.com.fiap.clinica.web.dto;

import br.com.fiap.clinica.domain.model.Especialidade;
import br.com.fiap.clinica.domain.model.Medico;

import java.util.UUID;

public record MedicoResponse(UUID id, String nome, String crm, Especialidade especialidade) {

    public static MedicoResponse de(Medico medico) {
        return new MedicoResponse(medico.getId(), medico.getNome(),
                medico.getCrm().toString(), medico.getEspecialidade());
    }
}
