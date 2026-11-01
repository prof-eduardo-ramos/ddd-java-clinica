package br.com.fiap.clinica.infrastructure.repository;

import br.com.fiap.clinica.domain.interfaces.ConsultaRepository;
import br.com.fiap.clinica.domain.model.Consulta;
import br.com.fiap.clinica.domain.model.Medico;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Adaptador: implementa a porta ConsultaRepository guardando as consultas
 * em memória, num Map (id -> consulta). Os dados somem quando o programa termina.
 */
public class ConsultaRepositoryEmMemoria implements ConsultaRepository {

    private final Map<UUID, Consulta> consultas = new HashMap<>();

    @Override
    public Consulta salvar(Consulta consulta) {
        consultas.put(consulta.getId(), consulta);
        return consulta;
    }

    @Override
    public Optional<Consulta> buscarPorId(UUID id) {
        return Optional.ofNullable(consultas.get(id));
    }

    @Override
    public List<Consulta> listarTodas() {
        return new ArrayList<>(consultas.values());
    }

    @Override
    public boolean existeConsultaAgendadaParaMedico(Medico medico, LocalDateTime dataHora) {
        for (Consulta consulta : consultas.values()) {
            // O mesmo médico é identificado pelo CRM (objeto de valor: compara pelo valor)
            if (consulta.estaAgendada()
                    && consulta.getMedico().getCrm().equals(medico.getCrm())
                    && consulta.getDataHora().equals(dataHora)) {
                return true;
            }
        }
        return false;
    }
}
