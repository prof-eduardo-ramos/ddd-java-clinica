package br.com.fiap.clinica.domain.model;

import java.util.UUID;

/**
 * Entidade que representa um médico da clínica.
 */
public class Medico {

    private final UUID id;
    private String nome;
    private final Crm crm;
    private Especialidade especialidade;

    public Medico(String nome, Crm crm, Especialidade especialidade) {
        if (crm == null) {
            throw new RegraDeNegocioException("CRM é obrigatório");
        }
        this.id = UUID.randomUUID();
        this.crm = crm;
        setNome(nome);
        setEspecialidade(especialidade);
    }

    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.trim().length() < 3) {
            throw new RegraDeNegocioException("Nome do médico deve ter ao menos 3 caracteres");
        }
        this.nome = nome.trim();
    }

    public Crm getCrm() {
        return crm;
    }

    public Especialidade getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(Especialidade especialidade) {
        if (especialidade == null) {
            throw new RegraDeNegocioException("Especialidade é obrigatória");
        }
        this.especialidade = especialidade;
    }
}
