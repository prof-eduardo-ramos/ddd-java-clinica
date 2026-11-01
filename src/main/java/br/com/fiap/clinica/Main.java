package br.com.fiap.clinica;

import br.com.fiap.clinica.application.service.ConsultaService;
import br.com.fiap.clinica.domain.interfaces.ConsultaRepository;
import br.com.fiap.clinica.domain.model.Consulta;
import br.com.fiap.clinica.domain.model.Cpf;
import br.com.fiap.clinica.domain.model.Crm;
import br.com.fiap.clinica.domain.model.Email;
import br.com.fiap.clinica.domain.model.Especialidade;
import br.com.fiap.clinica.domain.model.Medico;
import br.com.fiap.clinica.domain.model.Paciente;
import br.com.fiap.clinica.domain.model.RegraDeNegocioException;
import br.com.fiap.clinica.infrastructure.repository.ConsultaRepositoryEmMemoria;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

/**
 * Agendamento de consultas usando toda a estrutura criada,
 * SEM nenhum framework: nós mesmos criamos e ligamos os objetos.
 */
public class Main {

    public static void main(String[] args) {
        // 1. Infraestrutura: onde as consultas ficam guardadas
        ConsultaRepository repository = new ConsultaRepositoryEmMemoria();

        // 2. Aplicação: o caso de uso recebe o repositório e o relógio
        ConsultaService consultaService = new ConsultaService(repository, Clock.systemDefaultZone());

        // 3. Domínio: paciente e médico
        Paciente ana = new Paciente("Ana Souza", new Cpf("529.982.247-25"), new Email("ana@email.com"));
        Medico carlos = new Medico("Carlos Lima", new Crm("123456", "SP"), Especialidade.CARDIOLOGIA);

        // 4. Agendar uma consulta para a segunda-feira da próxima semana, às 10h
        LocalDateTime dataHora = LocalDate.now().plusWeeks(1)
                .with(TemporalAdjusters.next(DayOfWeek.MONDAY))
                .atTime(10, 0);
        Consulta consulta = consultaService.agendar(ana, carlos, dataHora);
        System.out.println("Consulta agendada: " + consulta.getPaciente().getNome()
                + " com " + consulta.getMedico().getNome()
                + " em " + consulta.getDataHora() + " -> " + consulta.getStatus());

        // 5. Regra 3: o mesmo médico no mesmo horário é recusado
        try {
            consultaService.agendar(ana, carlos, dataHora);
        } catch (RegraDeNegocioException e) {
            System.out.println("Agendamento recusado: " + e.getMessage());
        }

        // 6. Regra 2: domingo é recusado
        try {
            consultaService.agendar(ana, carlos, dataHora.minusDays(1));
        } catch (RegraDeNegocioException e) {
            System.out.println("Agendamento recusado: " + e.getMessage());
        }

        // 7. Cancelar a consulta (regra 4)
        consultaService.cancelar(consulta.getId(), "Paciente viajou");
        System.out.println("Status após cancelamento: " + consulta.getStatus());

        System.out.println("Consultas registradas: " + consultaService.listar().size());
    }
}
