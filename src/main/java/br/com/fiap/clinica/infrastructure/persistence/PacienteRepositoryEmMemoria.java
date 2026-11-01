package br.com.fiap.clinica.infrastructure.persistence;

import br.com.fiap.clinica.domain.model.Cpf;
import br.com.fiap.clinica.domain.model.Paciente;
import br.com.fiap.clinica.domain.repository.PacienteRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/** Adaptador: implementa a porta do domínio guardando os dados em um Map. */
public class PacienteRepositoryEmMemoria implements PacienteRepository {

    private final Map<UUID, Paciente> dados = new ConcurrentHashMap<>();

    @Override
    public Paciente salvar(Paciente paciente) {
        dados.put(paciente.getId(), paciente);
        return paciente;
    }

    @Override
    public Optional<Paciente> buscarPorId(UUID id) {
        return Optional.ofNullable(dados.get(id));
    }

    @Override
    public List<Paciente> listarTodos() {
        return List.copyOf(dados.values());
    }

    @Override
    public boolean existePorCpf(Cpf cpf) {
        return dados.values().stream().anyMatch(p -> p.getCpf().equals(cpf));
    }
}
