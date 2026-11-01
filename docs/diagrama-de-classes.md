# Diagrama de classes

O diagrama está em [Mermaid](https://mermaid.js.org/): é texto, fica versionado no Git junto com o código e o GitHub desenha automaticamente.

## Modelo de domínio

```mermaid
classDiagram
    direction LR

    class Consulta {
        <<Aggregate Root>>
        -UUID id
        -UUID pacienteId
        -UUID medicoId
        -LocalDateTime dataHora
        -StatusConsulta status
        -String motivoCancelamento
        +agendar(pacienteId, medicoId, dataHora, agora)$ Consulta
        +cancelar(motivo, agora) void
        +concluir() void
        +estaAgendada() boolean
    }

    class Paciente {
        <<Entity>>
        -UUID id
        -String nome
        -Cpf cpf
        -Email email
        +alterarEmail(Email) void
    }

    class Medico {
        <<Entity>>
        -UUID id
        -String nome
        -Crm crm
        -Especialidade especialidade
    }

    class Cpf {
        <<Value Object>>
        +String numero
        +formatado() String
    }

    class Email {
        <<Value Object>>
        +String endereco
    }

    class Crm {
        <<Value Object>>
        +String numero
        +String uf
    }

    class Especialidade {
        <<enumeration>>
        CLINICA_GERAL
        CARDIOLOGIA
        DERMATOLOGIA
        ORTOPEDIA
        PEDIATRIA
    }

    class StatusConsulta {
        <<enumeration>>
        AGENDADA
        CANCELADA
        REALIZADA
    }

    Paciente *-- Cpf
    Paciente *-- Email
    Medico *-- Crm
    Medico --> Especialidade
    Consulta --> StatusConsulta
    Consulta ..> Paciente : pacienteId
    Consulta ..> Medico : medicoId
```

### Como ler

- **Linha cheia com losango (`*--`)**: composição. O CPF faz parte do paciente e não existe sozinho no modelo.
- **Linha tracejada (`..>`)**: a `Consulta` só guarda o **id** do paciente e do médico. Um agregado não carrega outro agregado inteiro dentro de si; ele aponta pela identidade.
- **`$`** no método `agendar`: método estático (fábrica). O construtor é privado, então toda consulta nasce validada.
- **`-` e `+`**: atributos privados (encapsulamento) e métodos públicos (comportamento).

## Camadas (Clean Architecture)

```mermaid
flowchart LR
    subgraph web["web (Spring MVC)"]
        C[Controllers + DTOs]
    end
    subgraph app["application"]
        S[ConsultaService<br/>PacienteService<br/>MedicoService]
    end
    subgraph dom["domain (Java puro)"]
        M[Consulta, Paciente, Medico<br/>Cpf, Email, Crm]
        P[[ConsultaRepository<br/>PacienteRepository<br/>MedicoRepository]]
    end
    subgraph infra["infrastructure"]
        R[Repositórios em memória]
        CFG[BeanConfig]
    end

    C --> S
    S --> M
    S --> P
    R -. implementa .-> P
    CFG -. monta .-> S
```

**Regra de dependência:** as setas apontam sempre para dentro, em direção ao domínio. O domínio não conhece ninguém: nem Spring, nem banco, nem HTTP.
