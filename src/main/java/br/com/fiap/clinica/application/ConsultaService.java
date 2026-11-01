package br.com.fiap.clinica.application;

import br.com.fiap.clinica.domain.exception.RegraDeNegocioException;
import br.com.fiap.clinica.domain.model.Consulta;
import br.com.fiap.clinica.domain.repository.ConsultaRepository;
import br.com.fiap.clinica.domain.repository.MedicoRepository;
import br.com.fiap.clinica.domain.repository.PacienteRepository;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Caso de uso de consultas.
 * Regras que envolvem OUTRAS consultas (conflito de horário) ficam aqui,
 * porque uma consulta sozinha não sabe da existência das outras.
 */
public class ConsultaService {

    private final ConsultaRepository consultas;
    private final PacienteRepository pacientes;
    private final MedicoRepository medicos;
    private final Clock relogio;

    public ConsultaService(ConsultaRepository consultas, PacienteRepository pacientes,
                           MedicoRepository medicos, Clock relogio) {
        this.consultas = consultas;
        this.pacientes = pacientes;
        this.medicos = medicos;
        this.relogio = relogio;
    }

    public Consulta agendar(UUID pacienteId, UUID medicoId, LocalDateTime dataHora) {
        if (pacientes.buscarPorId(pacienteId).isEmpty()) {
            throw new RecursoNaoEncontradoException("Paciente não encontrado: " + pacienteId);
        }
        if (medicos.buscarPorId(medicoId).isEmpty()) {
            throw new RecursoNaoEncontradoException("Médico não encontrado: " + medicoId);
        }
        if (consultas.existeConsultaAgendadaParaMedico(medicoId, dataHora)) {
            throw new RegraDeNegocioException("Médico já possui consulta neste horário");
        }
        Consulta consulta = Consulta.agendar(pacienteId, medicoId, dataHora, LocalDateTime.now(relogio));
        return consultas.salvar(consulta);
    }

    public Consulta cancelar(UUID consultaId, String motivo) {
        Consulta consulta = buscar(consultaId);
        consulta.cancelar(motivo, LocalDateTime.now(relogio));
        return consultas.salvar(consulta);
    }

    public Consulta concluir(UUID consultaId) {
        Consulta consulta = buscar(consultaId);
        consulta.concluir();
        return consultas.salvar(consulta);
    }

    public Consulta buscar(UUID id) {
        return consultas.buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Consulta não encontrada: " + id));
    }

    public List<Consulta> listar() {
        return consultas.listarTodas();
    }
}
