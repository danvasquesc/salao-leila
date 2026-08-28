# Regras de Negócio

## Agendamento com múltiplos serviços

Um agendamento pode conter um ou mais serviços.

Os serviços são representados pela entidade associativa `AgendamentoServico`.

O mesmo serviço não pode ser informado duas vezes no mesmo agendamento.

## Disponibilidade

A disponibilidade considera a duração total de todos os serviços solicitados.

Agendamentos cancelados são desconsiderados na verificação de conflito.

## Data e horário

Não é permitido criar ou mover um agendamento para data e horário no passado.

## Alteração pelo cliente

A rota de alteração do cliente verifica a data original do agendamento.

Quando faltarem menos de 2 dias:

```text
alteração pelo sistema → bloqueada
alteração por telefone → necessária
```

## Alteração pela Leila

O painel operacional possui uma rota própria para registrar solicitações recebidas por telefone.

Ela não aplica o bloqueio de 2 dias, mas continua respeitando data, horário, serviços e disponibilidade.

## Histórico

A data inicial não pode ser posterior à data final.

Quando não existem registros, a resposta é uma lista vazia.

## Status geral

```text
AGENDADO
CONFIRMADO
CONCLUIDO
CANCELADO
```

O cancelamento geral também cancela os itens associados.

## Status individual

```text
AGENDADO
EM_ATENDIMENTO
CONCLUIDO
CANCELADO
```

Um serviço não pode ser alterado individualmente depois que o agendamento geral estiver cancelado.

## Identificação do cliente

O telefone é utilizado para localizar um cliente existente. Quando não existe cadastro, a interface cria um novo cliente antes de criar o agendamento.

O telefone aceita 10 ou 11 dígitos.
