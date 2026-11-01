package br.com.fiap.clinica.infrastructure.persistence;

import br.com.fiap.clinica.domain.model.Crm;
import br.com.fiap.clinica.domain.model.Medico;
import br.com.fiap.clinica.domain.repository.MedicoRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MedicoRepositoryEmMemoria implements MedicoRepository {

    private final Map<UUID, Medico> dados = new ConcurrentHashMap<>();

    @Override
    public Medico salvar(Medico medico) {
        dados.put(medico.getId(), medico);
        return medico;
    }

    @Override
    public Optional<Medico> buscarPorId(UUID id) {
        return Optional.ofNullable(dados.get(id));
    }

    @Override
    public List<Medico> listarTodos() {
        return List.copyOf(dados.values());
    }

    @Override
    public boolean existePorCrm(Crm crm) {
        return dados.values().stream().anyMatch(m -> m.getCrm().equals(crm));
    }
}
