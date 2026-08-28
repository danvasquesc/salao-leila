# API

Base local:

```text
http://localhost:8080
```

A API utiliza JSON nas operações de negócio.

## Clientes

### Criar cliente

```http
POST /clientes
```

```json
{
  "nome": "Mariana Oliveira",
  "telefone": "14992345678",
  "email": "mariana@email.com"
}
```

Esperado: `201 Created`.

### Listar clientes

```http
GET /clientes
```

### Buscar por ID

```http
GET /clientes/{id}
```

### Buscar por telefone

```http
GET /clientes/buscar?telefone=14992345678
```

### Atualizar

```http
PUT /clientes/{id}
```

### Excluir

```http
DELETE /clientes/{id}
```

Esperado: `204 No Content`.

## Serviços

### Criar

```http
POST /servicos
```

```json
{
  "nome": "Corte feminino",
  "descricao": "Corte feminino",
  "preco": 70.00,
  "duracao": 60
}
```

### Listar

```http
GET /servicos
```

### Buscar

```http
GET /servicos/{id}
```

### Atualizar

```http
PUT /servicos/{id}
```

### Excluir

```http
DELETE /servicos/{id}
```

## Agendamentos

### Criar

```http
POST /agendamentos
```

```json
{
  "data": "2026-09-10",
  "horario": "14:00",
  "clienteId": 4,
  "servicoIds": [1, 2]
}
```

Esperado: `201 Created`.

### Listar

```http
GET /agendamentos
```

### Buscar

```http
GET /agendamentos/{id}
```

### Alterar pela rota do cliente

```http
PUT /agendamentos/{id}
```

```json
{
  "data": "2026-09-12",
  "horario": "15:00",
  "servicoIds": [2]
}
```

Quando o agendamento original estiver a menos de 2 dias, o esperado é `400 Bad Request`.

### Histórico

```http
GET /agendamentos/historico?clienteId=4&de=2026-08-01&ate=2026-08-31
```

## Operacional

As rotas `/operacional/**` exigem autenticação.

### Agenda por data

```http
GET /operacional/agendamentos?data=2026-08-28
```

### Alterar agendamento pela Leila

```http
PUT /operacional/agendamentos/{id}
```

```json
{
  "data": "2026-08-28",
  "horario": "16:00",
  "servicoIds": [2, 3]
}
```

Essa operação é destinada a solicitações recebidas por telefone e não aplica o bloqueio de 2 dias da rota do cliente. As demais validações continuam sendo aplicadas.

### Atualizar status geral

```http
PATCH /operacional/agendamentos/{id}/status
```

```json
{
  "status": "CONFIRMADO"
}
```

Status gerais:

```text
AGENDADO
CONFIRMADO
CONCLUIDO
CANCELADO
```

### Atualizar status de serviço

```http
PATCH /operacional/agendamentos/{agendamentoId}/servicos/{itemId}/status
```

```json
{
  "status": "EM_ATENDIMENTO"
}
```

Status do serviço:

```text
AGENDADO
EM_ATENDIMENTO
CONCLUIDO
CANCELADO
```

## Autenticação operacional

```http
POST /login
```

Formulário:

```text
username=salao.leila
password=SalaoLeila@0
```

Logout:

```http
POST /logout
```

## Códigos HTTP

| Código | Significado                            |
|--------|----------------------------------------|
| `200`  | Operação realizada                     |
| `201`  | Registro criado                        |
| `204`  | Exclusão realizada                     |
| `400`  | Dados ou regra de negócio inválidos    |
| `404`  | Recurso não encontrado                 |
| `409`  | Conflito de disponibilidade ou estado  |
