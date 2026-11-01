package br.com.fiap.clinica.domain.interfaces;

import br.com.fiap.clinica.domain.model.Consulta;
import br.com.fiap.clinica.domain.model.Medico;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Porta: o domínio diz O QUE precisa para guardar consultas.
 * COMO isso é feito (memória, banco de dados...) fica na infraestrutura.
 */
public interface ConsultaRepository {

    Consulta salvar(Consulta consulta);

    Optional<Consulta> buscarPorId(UUID id);

    List<Consulta> listarTodas();

    boolean existeConsultaAgendadaParaMedico(Medico medico, LocalDateTime dataHora);
}
