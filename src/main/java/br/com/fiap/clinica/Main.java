package br.com.fiap.clinica;

public class Main {

    public static void main(String[] args) {
        Paciente paciente = new Paciente();
        paciente.setNome("Ana Souza");
        paciente.setEmail(new Email("ana@email.com"));

        System.out.println("Paciente: " + paciente.getNome());
        System.out.println("E-mail: " + paciente.getEmail().endereco());
        System.out.println("Id: " + paciente.getId());

        // O problema: o modelo aceita qualquer coisa!
        paciente.setNome("");
        Cpf cpf = new Cpf("123");
        Email email = new Email("isso-nao-e-um-email");

        System.out.println("Nome vazio aceito: '" + paciente.getNome() + "'");
        System.out.println("CPF inválido aceito: " + cpf.numero());
        System.out.println("E-mail inválido aceito: " + email.endereco());
    }
}
