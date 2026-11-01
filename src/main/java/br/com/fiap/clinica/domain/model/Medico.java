package br.com.fiap.clinica.domain.model;

import br.com.fiap.clinica.domain.exception.RegraDeNegocioException;

import java.util.Objects;
import java.util.UUID;

/** Entidade que representa um médico da clínica. */
public class Medico {

    private final UUID id;
    private final String nome;
    private final Crm crm;
    private final Especialidade especialidade;

    public Medico(String nome, Crm crm, Especialidade especialidade) {
        if (nome == null || nome.trim().length() < 3) {
            throw new RegraDeNegocioException("Nome do médico deve ter ao menos 3 caracteres");
        }
        this.id = UUID.randomUUID();
        this.nome = nome.trim();
        this.crm = Objects.requireNonNull(crm, "CRM é obrigatório");
        this.especialidade = Objects.requireNonNull(especialidade, "Especialidade é obrigatória");
    }

    public UUID getId() { return id; }
    public String getNome() { return nome; }
    public Crm getCrm() { return crm; }
    public Especialidade getEspecialidade() { return especialidade; }

    @Override
    public boolean equals(Object o) {
        return o instanceof Medico outro && id.equals(outro.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
