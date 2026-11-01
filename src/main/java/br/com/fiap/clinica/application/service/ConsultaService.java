package br.com.fiap.clinica.application.service;

import br.com.fiap.clinica.domain.interfaces.ConsultaRepository;
import br.com.fiap.clinica.domain.model.Consulta;
import br.com.fiap.clinica.domain.model.Medico;
import br.com.fiap.clinica.domain.model.Paciente;
import br.com.fiap.clinica.domain.model.RegraDeNegocioException;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Caso de uso: orquestra o domínio e o repositório.
 * Não sabe se as consultas estão na memória ou num banco de dados.
 */
public class ConsultaService {

    private final ConsultaRepository repository;
    private final Clock relogio;

    // Injeção de dependência: o service recebe o repositório pronto
    public ConsultaService(ConsultaRepository repository, Clock relogio) {
        this.repository = repository;
        this.relogio = relogio;
    }

    public Consulta agendar(Paciente paciente, Medico medico, LocalDateTime dataHora) {
        // Regra 3: depende das OUTRAS consultas, por isso fica no caso de uso
        if (repository.existeConsultaAgendadaParaMedico(medico, dataHora)) {
            throw new RegraDeNegocioException("Médico já possui consulta neste horário");
        }
        Consulta consulta = new Consulta(paciente, medico, dataHora, agora());
        return repository.salvar(consulta);
    }

    public Consulta cancelar(UUID consultaId, String motivo) {
        Consulta consulta = buscar(consultaId);
        consulta.cancelar(motivo, agora());
        return repository.salvar(consulta);
    }

    public Consulta concluir(UUID consultaId) {
        Consulta consulta = buscar(consultaId);
        consulta.concluir();
        return repository.salvar(consulta);
    }

    public Consulta buscar(UUID consultaId) {
        return repository.buscarPorId(consultaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Consulta não encontrada: " + consultaId));
    }

    public List<Consulta> listar() {
        return repository.listarTodas();
    }

    private LocalDateTime agora() {
        return LocalDateTime.now(relogio);
    }
}
