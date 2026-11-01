# Diagrama de classes

O diagrama está em [Mermaid](https://mermaid.js.org/): é texto, fica versionado no Git junto com o código e o GitHub desenha automaticamente.

## Modelo de domínio

```mermaid
classDiagram
    direction LR

    class Consulta {
        <<Aggregate Root>>
        -UUID id
        -Paciente paciente
        -Medico medico
        -LocalDateTime dataHora
        -StatusConsulta status
        -String motivoCancelamento
        +Consulta(paciente, medico, dataHora, agora)
        +cancelar(motivo, agora) void
        +concluir() void
        +getters()
    }

    class Paciente {
        <<Entity>>
        -UUID id
        -String nome
        -Cpf cpf
        -Email email
        +Paciente(nome, cpf, email)
        +getters()
        +setNome(String) void
        +setEmail(Email) void
    }

    class Medico {
        <<Entity>>
        -UUID id
        -String nome
        -Crm crm
        -Especialidade especialidade
        +Medico(nome, crm, especialidade)
        +getters()
        +setNome(String) void
        +setEspecialidade(Especialidade) void
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
    Consulta "*" --> "1" Paciente : paciente
    Consulta "*" --> "1" Medico : medico
```

### Como ler

- **Linha cheia com losango (`*--`)**: composição. O CPF faz parte do paciente e não existe sozinho no modelo.
- **Seta cheia (`-->`) com `1` e `*`**: associação. Toda `Consulta` **tem um** `Paciente` e **tem um** `Medico` (atributos do tipo dessas classes); um paciente ou médico pode ter **várias** consultas. Pelo objeto, navegamos: `consulta.getPaciente().getNome()`.
- **`-` e `+`**: atributos privados (encapsulamento) e métodos públicos.
- **Setters só quando fazem sentido**: o nome e o e-mail do paciente podem mudar; o `id` e o CPF, não. A `Consulta` não tem setters: o status só muda por `cancelar()` e `concluir()`.

## Camadas (Clean Architecture)

```mermaid
flowchart LR
    subgraph presentation["presentation (Spring MVC)"]
        C[ConsultaController + DTOs]
    end
    subgraph app["application.service"]
        S[ConsultaService]
    end
    subgraph dom["domain"]
        M[domain.model<br/>Consulta, Paciente, Medico<br/>Cpf, Email, Crm]
        P[[domain.interfaces<br/>ConsultaRepository]]
    end
    subgraph infra["infrastructure"]
        R[infrastructure.repository<br/>ConsultaRepositoryEmMemoria]
        CFG[infrastructure.config<br/>BeanConfig]
    end

    C --> S
    S --> M
    S --> P
    R -. implementa .-> P
    CFG -. monta .-> S
```

**Regra de dependência:** as setas apontam sempre para dentro, em direção ao domínio. O domínio não conhece ninguém: nem Spring, nem banco, nem HTTP.
