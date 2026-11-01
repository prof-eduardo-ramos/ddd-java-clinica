package br.com.fiap.clinica.domain.model;

import java.util.UUID;

/**
 * Entidade: tem identidade (id) que não muda, mesmo que os outros dados mudem.
 */
public class Paciente {

    private final UUID id;
    private String nome;
    private final Cpf cpf;
    private Email email;

    // Construtor: só existe paciente com nome, CPF e e-mail válidos
    public Paciente(String nome, Cpf cpf, Email email) {
        if (cpf == null) {
            throw new RegraDeNegocioException("CPF é obrigatório");
        }
        this.id = UUID.randomUUID();
        this.cpf = cpf;
        setNome(nome);
        setEmail(email);
    }

    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    // O setter também protege a invariante: nome com pelo menos 3 caracteres
    public void setNome(String nome) {
        if (nome == null || nome.trim().length() < 3) {
            throw new RegraDeNegocioException("Nome do paciente deve ter ao menos 3 caracteres");
        }
        this.nome = nome.trim();
    }

    public Cpf getCpf() {
        return cpf;
    }

    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        if (email == null) {
            throw new RegraDeNegocioException("E-mail é obrigatório");
        }
        this.email = email;
    }
}
