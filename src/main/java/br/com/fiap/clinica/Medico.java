package br.com.fiap.clinica;

import java.util.UUID;

public class Medico {

    private UUID id = UUID.randomUUID();
    private String nome;
    private Crm crm;
    private Especialidade especialidade;

    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Crm getCrm() {
        return crm;
    }

    public Especialidade getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(Especialidade especialidade) {
        this.especialidade = especialidade;
    }
}
