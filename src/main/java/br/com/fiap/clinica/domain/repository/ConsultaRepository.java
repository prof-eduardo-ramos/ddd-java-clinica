package br.com.fiap.clinica.domain.repository;

import br.com.fiap.clinica.domain.model.Consulta;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConsultaRepository {
    Consulta salvar(Consulta consulta);
    Optional<Consulta> buscarPorId(UUID id);
    List<Consulta> listarTodas();
    boolean existeConsultaAgendadaParaMedico(UUID medicoId, LocalDateTime dataHora);
}
