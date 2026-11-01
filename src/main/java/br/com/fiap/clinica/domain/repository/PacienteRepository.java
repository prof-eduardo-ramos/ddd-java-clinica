package br.com.fiap.clinica.domain.repository;

import br.com.fiap.clinica.domain.model.Cpf;
import br.com.fiap.clinica.domain.model.Paciente;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Porta (Clean Architecture): o domínio diz O QUE precisa,
 * a infraestrutura decide COMO (memória, JPA, MongoDB...).
 */
public interface PacienteRepository {
    Paciente salvar(Paciente paciente);
    Optional<Paciente> buscarPorId(UUID id);
    List<Paciente> listarTodos();
    boolean existePorCpf(Cpf cpf);
}
