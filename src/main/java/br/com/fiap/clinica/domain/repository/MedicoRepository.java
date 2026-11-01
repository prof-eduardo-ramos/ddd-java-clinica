package br.com.fiap.clinica.domain.repository;

import br.com.fiap.clinica.domain.model.Crm;
import br.com.fiap.clinica.domain.model.Medico;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MedicoRepository {
    Medico salvar(Medico medico);
    Optional<Medico> buscarPorId(UUID id);
    List<Medico> listarTodos();
    boolean existePorCrm(Crm crm);
}
