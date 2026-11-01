package br.com.fiap.clinica;

import java.util.UUID;

public class Paciente {

    private UUID id = UUID.randomUUID();
    private String nome;
    private Cpf cpf;
    private Email email;

    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Cpf getCpf() {
        return cpf;
    }

    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        this.email = email;
    }
}
