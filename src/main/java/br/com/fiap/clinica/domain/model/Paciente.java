package br.com.fiap.clinica.domain.model;

import br.com.fiap.clinica.domain.exception.RegraDeNegocioException;

import java.util.Objects;
import java.util.UUID;

/**
 * Entidade: tem identidade (id) que não muda, mesmo que os dados mudem.
 * Dois pacientes com o mesmo nome continuam sendo pessoas diferentes.
 */
public class Paciente {

    private final UUID id;
    private String nome;
    private final Cpf cpf;
    private Email email;

    public Paciente(String nome, Cpf cpf, Email email) {
        this.id = UUID.randomUUID();
        this.nome = validarNome(nome);
        this.cpf = Objects.requireNonNull(cpf, "CPF é obrigatório");
        this.email = Objects.requireNonNull(email, "E-mail é obrigatório");
    }

    /** Comportamento em vez de setter: a intenção fica explícita. */
    public void alterarEmail(Email novoEmail) {
        this.email = Objects.requireNonNull(novoEmail, "E-mail é obrigatório");
    }

    private static String validarNome(String nome) {
        if (nome == null || nome.trim().length() < 3) {
            throw new RegraDeNegocioException("Nome do paciente deve ter ao menos 3 caracteres");
        }
        return nome.trim();
    }

    public UUID getId() { return id; }
    public String getNome() { return nome; }
    public Cpf getCpf() { return cpf; }
    public Email getEmail() { return email; }

    // Entidades são comparadas pela identidade, não pelos atributos
    @Override
    public boolean equals(Object o) {
        return o instanceof Paciente outro && id.equals(outro.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
