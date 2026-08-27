# Testes e Evidências

## Estratégia de testes

Durante o desenvolvimento, a aplicação foi validada por meio de:

- testes manuais da API utilizando Postman;
- testes da integração frontend/API pelo navegador;
- consultas diretas ao MySQL utilizando MySQL Workbench.

Os testes unitários automatizados não fazem parte do escopo final desta entrega. A prioridade foi validar os principais fluxos e regras de negócio da aplicação dentro do prazo disponível.


# Testes principais da API

Os testes abaixo foram selecionados como principais evidências do funcionamento da API.


## 1. Listagem de serviços

**Método**

`GET`

**Endpoint**

`/servicos`

**Resultado esperado**

`200 OK`

A resposta deve apresentar os serviços cadastrados, incluindo nome, descrição, preço e duração.


## 2. Criação de agendamento com múltiplos serviços

**Método**

`POST`

**Endpoint**

`/agendamentos`

**Body**

```json
{
  "data": "DATA_FUTURA",
  "horario": "14:00",
  "clienteId": 4,
  "servicoIds": [4, 6]
}
```

**Resultado esperado**

`201 Created`

A resposta deve apresentar:

- cliente;
- data;
- horário;
- status AGENDADO;
- os dois serviços selecionados.


## 3. Alteração de agendamento

**Método**

`PUT`

**Endpoint**

`/agendamentos/{id}`

**Body**

```json
{
  "data": "DATA_FUTURA",
  "horario": "15:00",
  "servicoIds": [4]
}
```

**Resultado esperado**

`200 OK`

O agendamento deve ser atualizado respeitando as regras de prazo e disponibilidade.


## 4. Regra de conflito de horário

**Método**

`POST` ou `PUT`

**Endpoint**

`/agendamentos`

**Resultado esperado**

`409 Conflict`

O sistema deve impedir a criação ou alteração quando o horário solicitado conflitar com outro atendimento.


## 5. Regra de alteração com menos de dois dias

Método

PUT

Endpoint

/agendamentos/{id}

Resultado esperado

400 Bad Request

O sistema deve impedir que o cliente altere pelo sistema um agendamento com menos de dois dias de antecedência.

6. Histórico do cliente

Método

GET

Endpoint

/agendamentos/historico?clienteId={id}&de={dataInicial}&ate={dataFinal}

Resultado esperado

200 OK

A resposta deve apresentar os agendamentos do cliente dentro do período solicitado, incluindo seus detalhes e status.

7. Agenda operacional

Método

GET

Endpoint

/operacional/agendamentos?data={data}

Resultado esperado

200 OK

A resposta deve apresentar:

horário;
cliente;
telefone;
status do agendamento;
serviços;
status individual dos serviços.
8. Confirmação operacional

Método

PATCH

Endpoint

/operacional/agendamentos/{id}/status

Body

{
  "status": "CONFIRMADO"
}

Resultado esperado

200 OK

9. Atualização do status de um serviço

Método

PATCH

Endpoint

/operacional/agendamentos/{agendamentoId}/servicos/{itemId}/status

Body

{
  "status": "EM_ATENDIMENTO"
}

Resultado esperado

200 OK

O status deve ser alterado somente para o serviço correspondente.