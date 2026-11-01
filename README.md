# ddd-java-clinica

Projeto da live **"Diagramando e criando as classes com Java"** da disciplina *Domain Driven Design com Java* (Engenharia de Software, FIAP).

Modelamos o domínio de uma **clínica de consultas médicas**, implementamos as classes em **Java puro** com as regras de negócio dentro do modelo e, no fim, expomos tudo como uma **API REST com Spring Boot**, sem alterar o domínio.

```bash
git clone https://github.com/prof-eduardo-ramos/ddd-java-clinica.git
```

## Branches e tags

| Referência | Conteúdo |
|---|---|
| `main` | Solução completa (Java puro + API Spring Boot) |
| `lab/diagramando-classes-java` | **Ponto de partida da atividade**: estrutura pronta, classes com `TODO` e testes falhando |
| `etapa-1` | Modelagem: linguagem ubíqua, diagrama e estrutura de pacotes |
| `etapa-2` | Objetos de valor (`Cpf`, `Email`, `Crm`) e enums |
| `etapa-3` | Entidades, agregado `Consulta`, portas, casos de uso e demo em Java puro |
| `etapa-4` | API REST com Spring Boot |

## Como rodar

Pré-requisitos: **JDK 21** e **Maven 3.9+**.

```bash
mvn test                 # 36 testes
mvn spring-boot:run      # API em http://localhost:8080
```

Use o arquivo [`requests.http`](requests.http) para testar os endpoints.

## Documentação

- [Passo a passo do hands on](docs/PASSO-A-PASSO.md)
- [Linguagem ubíqua e regras de negócio](docs/linguagem-ubiqua.md)
- [Diagrama de classes e camadas](docs/diagrama-de-classes.md)

## Endpoints

| Método | Caminho | Descrição |
|---|---|---|
| POST | `/pacientes` | Cadastra paciente |
| GET | `/pacientes`, `/pacientes/{id}` | Lista / busca pacientes |
| POST | `/medicos` | Cadastra médico |
| GET | `/medicos`, `/medicos/{id}` | Lista / busca médicos |
| POST | `/consultas` | Agenda consulta |
| GET | `/consultas`, `/consultas/{id}` | Lista / busca consultas |
| POST | `/consultas/{id}/cancelamento` | Cancela (body: `{"motivo": "..."}`) |
| POST | `/consultas/{id}/conclusao` | Conclui |

## Estrutura

```
br.com.fiap.clinica
├── domain              Java puro: o coração do sistema
│   ├── model           Consulta (agregado), Paciente, Medico, Cpf, Email, Crm, enums
│   ├── exception       RegraDeNegocioException
│   └── repository      Portas (interfaces)
├── application         Casos de uso: PacienteService, MedicoService, ConsultaService
├── infrastructure
│   ├── persistence     Adaptadores em memória
│   └── config          BeanConfig (Spring)
├── web                 Controllers, DTOs e tratamento de erros
└── demo                DemoJavaPuro: o domínio rodando sem framework
```

**Stack:** Java 21 · Maven · JUnit 6 · Spring Boot 4.1
