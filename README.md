# ddd-java-clinica · Atividade

Você está na branch **`lab/diagramando-classes-java`**: o ponto de partida do hands on da live **"Diagramando e criando as classes com Java"** (FIAP · DDD com Java).

Aqui está um projeto Java **recém-criado**: um `pom.xml` e a classe `Main` com o método `main` vazio. A partir dele, você vai construir o sistema da clínica passo a passo.

```bash
git clone https://github.com/prof-eduardo-ramos/ddd-java-clinica.git
cd ddd-java-clinica
git checkout lab/diagramando-classes-java
```

## Roteiro

| Passo | O que você vai fazer | Pacote |
|---|---|---|
| 1 | Projeto Java do zero com `main` vazio (já pronto nesta branch) | `br.com.fiap.clinica` |
| 2 | Criar as classes do diagrama: atributos, getters e setters | `br.com.fiap.clinica` |
| 3 | Proteger as invariantes de negócio nos construtores e mover as classes | `domain.model` |
| 4 | Criar a porta `ConsultaRepository` e o caso de uso `ConsultaService` | `domain.interfaces`, `application.service` |
| 5 | Implementar o adaptador em memória | `infrastructure.repository` |
| 6 | Agendar consultas pelo `main` | `br.com.fiap.clinica` |
| 7 | Expor a API REST com Spring Boot | `presentation`, `infrastructure.config` |

Siga o **[passo a passo completo](docs/PASSO-A-PASSO.md)**: ele explica cada conceito de Java no momento em que aparece.

## Material de apoio

- [Linguagem ubíqua e regras de negócio](docs/linguagem-ubiqua.md)
- [Diagrama de classes e camadas](docs/diagrama-de-classes.md)

Travou? A solução está na branch `main`, e cada passo tem uma tag (`passo-1` a `passo-7`).
