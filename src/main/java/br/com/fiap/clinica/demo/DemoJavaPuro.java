package br.com.fiap.clinica.demo;

import br.com.fiap.clinica.application.ConsultaService;
import br.com.fiap.clinica.application.MedicoService;
import br.com.fiap.clinica.application.PacienteService;
import br.com.fiap.clinica.domain.exception.RegraDeNegocioException;
import br.com.fiap.clinica.domain.model.Consulta;
import br.com.fiap.clinica.domain.model.Especialidade;
import br.com.fiap.clinica.domain.model.Medico;
import br.com.fiap.clinica.domain.model.Paciente;
import br.com.fiap.clinica.infrastructure.persistence.ConsultaRepositoryEmMemoria;
import br.com.fiap.clinica.infrastructure.persistence.MedicoRepositoryEmMemoria;
import br.com.fiap.clinica.infrastructure.persistence.PacienteRepositoryEmMemoria;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;

/**
 * Etapa 3: o domínio funcionando SEM framework nenhum.
 * Nós mesmos criamos os objetos e ligamos as dependências ("na mão").
 */
public class DemoJavaPuro {

    public static void main(String[] args) {
        var pacientesRepo = new PacienteRepositoryEmMemoria();
        var medicosRepo = new MedicoRepositoryEmMemoria();
        var consultasRepo = new ConsultaRepositoryEmMemoria();

        var pacienteService = new PacienteService(pacientesRepo);
        var medicoService = new MedicoService(medicosRepo);
        var consultaService = new ConsultaService(consultasRepo, pacientesRepo, medicosRepo, Clock.systemDefaultZone());

        Paciente ana = pacienteService.cadastrar("Ana Souza", "529.982.247-25", "ana@email.com");
        Medico drCarlos = medicoService.cadastrar("Carlos Lima", "123456", "SP", Especialidade.CARDIOLOGIA);

        var segundaQueVem = LocalDate.now().plusWeeks(1).with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        Consulta consulta = consultaService.agendar(ana.getId(), drCarlos.getId(), segundaQueVem.atTime(LocalTime.of(10, 0)));
        System.out.println("Consulta agendada para " + consulta.getDataHora() + " -> " + consulta.getStatus());

        try {
            consultaService.agendar(ana.getId(), drCarlos.getId(), segundaQueVem.atTime(LocalTime.of(10, 0)));
        } catch (RegraDeNegocioException e) {
            System.out.println("Regra de negócio funcionando: " + e.getMessage());
        }

        consultaService.cancelar(consulta.getId(), "Paciente viajou");
        System.out.println("Status após cancelamento: " + consulta.getStatus());
    }
}
