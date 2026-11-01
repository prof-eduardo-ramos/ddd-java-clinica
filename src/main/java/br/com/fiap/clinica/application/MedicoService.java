package br.com.fiap.clinica.application;

import br.com.fiap.clinica.domain.exception.RegraDeNegocioException;
import br.com.fiap.clinica.domain.model.Crm;
import br.com.fiap.clinica.domain.model.Especialidade;
import br.com.fiap.clinica.domain.model.Medico;
import br.com.fiap.clinica.domain.repository.MedicoRepository;

import java.util.List;
import java.util.UUID;

public class MedicoService {

    private final MedicoRepository repository;

    public MedicoService(MedicoRepository repository) {
        this.repository = repository;
    }

    public Medico cadastrar(String nome, String numeroCrm, String uf, Especialidade especialidade) {
        Crm crm = new Crm(numeroCrm, uf);
        if (repository.existePorCrm(crm)) {
            throw new RegraDeNegocioException("Já existe médico com o " + crm);
        }
        return repository.salvar(new Medico(nome, crm, especialidade));
    }

    public Medico buscar(UUID id) {
        return repository.buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Médico não encontrado: " + id));
    }

    public List<Medico> listar() {
        return repository.listarTodos();
    }
}
