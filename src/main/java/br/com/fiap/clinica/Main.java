package br.com.fiap.clinica;

import br.com.fiap.clinica.domain.model.Cpf;
import br.com.fiap.clinica.domain.model.Crm;
import br.com.fiap.clinica.domain.model.Email;
import br.com.fiap.clinica.domain.model.Especialidade;
import br.com.fiap.clinica.domain.model.Medico;
import br.com.fiap.clinica.domain.model.Paciente;
import br.com.fiap.clinica.domain.model.RegraDeNegocioException;

public class Main {

    public static void main(String[] args) {
        Paciente ana = new Paciente("Ana Souza", new Cpf("529.982.247-25"), new Email("ana@email.com"));
        Medico carlos = new Medico("Carlos Lima", new Crm("123456", "SP"), Especialidade.CARDIOLOGIA);

        System.out.println("Paciente: " + ana.getNome() + " - CPF " + ana.getCpf().formatado());
        System.out.println("Médico: " + carlos.getNome() + " - " + carlos.getCrm());

        // Agora o modelo se protege: dados inválidos geram exceção
        try {
            ana.setNome("");
        } catch (RegraDeNegocioException e) {
            System.out.println("Invariante protegida: " + e.getMessage());
        }
        try {
            new Cpf("123");
        } catch (RegraDeNegocioException e) {
            System.out.println("Invariante protegida: " + e.getMessage());
        }
    }
}
