# Linguagem ubíqua: Clínica de Consultas

Linguagem ubíqua é o vocabulário que **especialistas do negócio e desenvolvedores usam igual**: na conversa, no diagrama e no código. Se a recepcionista diz "cancelar consulta", o método se chama `cancelar()`, e não `update()` ou `setStatus()`.

## O problema, contado por quem é do negócio

> "Nossa clínica atende **pacientes** com **médicos** de várias **especialidades**. O paciente **agenda uma consulta** com um médico em uma **data e hora**. A clínica funciona de **segunda a sábado, das 7h às 19h**, e cada consulta dura **1 hora**, sempre começando em hora cheia. Ninguém agenda em cima da hora: precisa de pelo menos **30 minutos de antecedência**. Um médico **não pode ter duas consultas no mesmo horário**. O paciente pode **cancelar**, mas precisa **informar o motivo** e avisar com **24 horas de antecedência**. Depois do atendimento, a consulta é **concluída**."

## Do texto para o modelo

Técnica simples para começar: **substantivos viram candidatos a classes**, **verbos viram candidatos a métodos**.

| Termo do negócio | Tipo no modelo | Por quê |
|---|---|---|
| Paciente | Entidade | Tem identidade: dois pacientes com o mesmo nome são pessoas diferentes |
| Médico | Entidade | Idem |
| Consulta | Entidade + **agregado raiz** | Tem ciclo de vida (agendada → cancelada/realizada) e concentra as regras |
| CPF, E-mail, CRM | Objeto de valor | Definidos só pelo valor; imutáveis; validam a si mesmos |
| Especialidade | Enum | Lista fechada de valores |
| Status da consulta | Enum | AGENDADA, CANCELADA, REALIZADA |
| agendar, cancelar, concluir | Construtor e métodos da `Consulta` | Comportamento mora junto dos dados |

## Regras de negócio

| # | Regra | Onde mora no código |
|---|---|---|
| 1 | Agendamento com no mínimo 30 minutos de antecedência | construtor de `Consulta` |
| 2 | Segunda a sábado, das 7h às 19h, em hora cheia (último horário 18h) | construtor de `Consulta` |
| 3 | Médico não pode ter duas consultas no mesmo horário | `ConsultaService.agendar()` (precisa consultar as outras consultas) |
| 4 | Cancelamento exige motivo e 24h de antecedência | `Consulta.cancelar()` |
| 5 | Só consulta agendada pode ser concluída | `Consulta.concluir()` |

**Pergunta para a turma:** por que a regra 3 não está dentro da classe `Consulta`?
Porque uma consulta sozinha não conhece as outras consultas. Regras que dependem de vários agregados ficam no caso de uso (camada de aplicação), que tem acesso ao repositório.
