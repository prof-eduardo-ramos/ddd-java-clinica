package br.com.fiap.clinica.infrastructure.persistence;

import br.com.fiap.clinica.domain.model.Consulta;
import br.com.fiap.clinica.domain.repository.ConsultaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ConsultaRepositoryEmMemoria implements ConsultaRepository {

    private final Map<UUID, Consulta> dados = new ConcurrentHashMap<>();

    @Override
    public Consulta salvar(Consulta consulta) {
        dados.put(consulta.getId(), consulta);
        return consulta;
    }

    @Override
    public Optional<Consulta> buscarPorId(UUID id) {
        return Optional.ofNullable(dados.get(id));
    }

    @Override
    public List<Consulta> listarTodas() {
        return List.copyOf(dados.values());
    }

    @Override
    public boolean existeConsultaAgendadaParaMedico(UUID medicoId, LocalDateTime dataHora) {
        return dados.values().stream()
                .anyMatch(c -> c.estaAgendada()
                        && c.getMedicoId().equals(medicoId)
                        && c.getDataHora().equals(dataHora));
    }
}
