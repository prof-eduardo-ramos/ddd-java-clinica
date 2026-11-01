package br.com.fiap.clinica.infrastructure.config;

import br.com.fiap.clinica.application.ConsultaService;
import br.com.fiap.clinica.application.MedicoService;
import br.com.fiap.clinica.application.PacienteService;
import br.com.fiap.clinica.domain.repository.ConsultaRepository;
import br.com.fiap.clinica.domain.repository.MedicoRepository;
import br.com.fiap.clinica.domain.repository.PacienteRepository;
import br.com.fiap.clinica.infrastructure.persistence.ConsultaRepositoryEmMemoria;
import br.com.fiap.clinica.infrastructure.persistence.MedicoRepositoryEmMemoria;
import br.com.fiap.clinica.infrastructure.persistence.PacienteRepositoryEmMemoria;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.ZoneId;

/**
 * É o mesmo "new" que fizemos no DemoJavaPuro, só que agora o Spring
 * cria e injeta os objetos. Domínio e application continuam SEM anotações do Spring.
 */
@Configuration
public class BeanConfig {

    @Bean
    public Clock relogio() {
        return Clock.system(ZoneId.of("America/Sao_Paulo"));
    }

    @Bean
    public PacienteRepository pacienteRepository() {
        return new PacienteRepositoryEmMemoria();
    }

    @Bean
    public MedicoRepository medicoRepository() {
        return new MedicoRepositoryEmMemoria();
    }

    @Bean
    public ConsultaRepository consultaRepository() {
        return new ConsultaRepositoryEmMemoria();
    }

    @Bean
    public PacienteService pacienteService(PacienteRepository repository) {
        return new PacienteService(repository);
    }

    @Bean
    public MedicoService medicoService(MedicoRepository repository) {
        return new MedicoService(repository);
    }

    @Bean
    public ConsultaService consultaService(ConsultaRepository consultas, PacienteRepository pacientes,
                                           MedicoRepository medicos, Clock relogio) {
        return new ConsultaService(consultas, pacientes, medicos, relogio);
    }
}
