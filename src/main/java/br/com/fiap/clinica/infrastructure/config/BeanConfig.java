package br.com.fiap.clinica.infrastructure.config;

import br.com.fiap.clinica.application.service.ConsultaService;
import br.com.fiap.clinica.domain.interfaces.ConsultaRepository;
import br.com.fiap.clinica.infrastructure.repository.ConsultaRepositoryEmMemoria;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.ZoneId;

/**
 * É o mesmo "new" que fizemos no Main, só que agora o Spring
 * cria e injeta os objetos. Domínio e aplicação continuam SEM anotações do Spring.
 */
@Configuration
public class BeanConfig {

    @Bean
    public Clock relogio() {
        return Clock.system(ZoneId.of("America/Sao_Paulo"));
    }

    @Bean
    public ConsultaRepository consultaRepository() {
        return new ConsultaRepositoryEmMemoria();
    }

    @Bean
    public ConsultaService consultaService(ConsultaRepository repository, Clock relogio) {
        return new ConsultaService(repository, relogio);
    }
}
