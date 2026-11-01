# ddd-java-clinica

Projeto da live **"Diagramando e criando as classes com Java"** da disciplina *Domain Driven Design com Java* (Engenharia de Software, FIAP).

Partimos de uma **aplicação Java vazia** e criamos, passo a passo, as classes de domínio de uma **clínica de consultas médicas** a partir do diagrama de classes. Depois protegemos as **invariantes de negócio** nos construtores, criamos a **porta** e o **caso de uso**, o **adaptador em memória**, agendamos consultas pelo `main` e, por fim, expomos tudo como uma **API REST com Spring Boot**, sem alterar o domínio.

```bash
git clone https://github.com/prof-eduardo-ramos/ddd-java-clinica.git
```

## Branches e tags

| Referência | Conteúdo |
|---|---|
| `main` | Solução completa (Java puro + API Spring Boot) |
| `lab/diagramando-classes-java` | **Ponto de partida da atividade**: projeto Java recém-criado, com o `main` vazio |
| `passo-1` | Projeto Java do zero com o método `main` vazio |
| `passo-2` | Classes de domínio do diagrama: só atributos, getters e setters |
| `passo-3` | Invariantes de negócio nos construtores; classes movidas para `domain.model` |
| `passo-4` | Porta `ConsultaRepository` (`domain.interfaces`) e caso de uso `ConsultaService` (`application.service`) |
| `passo-5` | Adaptador em memória `ConsultaRepositoryEmMemoria` (`infrastructure.repository`) |
| `passo-6` | `main` agendando consultas com toda a estrutura |
| `passo-7` | API REST com Spring Boot (`presentation` e `infrastructure.config`) |

## Como rodar

Pré-requisitos: **JDK 21** e **Maven 3.9+** (o IntelliJ IDEA já traz o Maven embutido).

```bash
mvn test                 # 38 testes
mvn spring-boot:run      # API em http://localhost:8080
```

A classe `Main` continua rodando o agendamento em Java puro, sem Spring. Use o arquivo [`requests.http`](requests.http) para testar a API.

## Documentação

- [Passo a passo do hands on](docs/PASSO-A-PASSO.md)
- [Linguagem ubíqua e regras de negócio](docs/linguagem-ubiqua.md)
- [Diagrama de classes e camadas](docs/diagrama-de-classes.md)

## Endpoints

| Método | Caminho | Descrição |
|---|---|---|
| POST | `/consultas` | Agenda consulta |
| GET | `/consultas`, `/consultas/{id}` | Lista / busca consultas |
| POST | `/consultas/{id}/cancelamento` | Cancela (body: `{"motivo": "..."}`) |
| POST | `/consultas/{id}/conclusao` | Conclui |

## Estrutura

```
br.com.fiap.clinica
├── Main                         Java puro: agendamento sem framework
├── ClinicaApplication           Spring Boot: sobe a API
├── domain
│   ├── model                    Consulta, Paciente, Medico, Cpf, Email, Crm, enums, RegraDeNegocioException
│   └── interfaces               ConsultaRepository (porta)
├── application
│   └── service                  ConsultaService (caso de uso)
├── infrastructure
│   ├── repository               ConsultaRepositoryEmMemoria (adaptador)
│   └── config                   BeanConfig (Spring)
└── presentation                 ConsultaController, DTOs e tratamento de erros
```

**Stack:** Java 21 · Maven · JUnit 6 · Spring Boot 4.1
