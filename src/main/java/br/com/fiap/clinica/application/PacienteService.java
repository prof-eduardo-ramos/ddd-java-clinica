package br.com.fiap.clinica.application;

import br.com.fiap.clinica.domain.exception.RegraDeNegocioException;
import br.com.fiap.clinica.domain.model.Cpf;
import br.com.fiap.clinica.domain.model.Email;
import br.com.fiap.clinica.domain.model.Paciente;
import br.com.fiap.clinica.domain.repository.PacienteRepository;

import java.util.List;
import java.util.UUID;

/** Caso de uso: orquestra o domínio e o repositório. Não conhece HTTP nem banco. */
public class PacienteService {

    private final PacienteRepository repository;

    public PacienteService(PacienteRepository repository) {
        this.repository = repository;
    }

    public Paciente cadastrar(String nome, String cpf, String email) {
        Cpf cpfValidado = new Cpf(cpf);
        if (repository.existePorCpf(cpfValidado)) {
            throw new RegraDeNegocioException("Já existe paciente com o CPF " + cpfValidado.formatado());
        }
        return repository.salvar(new Paciente(nome, cpfValidado, new Email(email)));
    }

    public Paciente buscar(UUID id) {
        return repository.buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Paciente não encontrado: " + id));
    }

    public List<Paciente> listar() {
        return repository.listarTodos();
    }
}
