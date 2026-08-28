# Testes

## Estratégia

A entrega utiliza principalmente testes manuais de integração por Postman, navegador e MySQL Workbench.

A issue dedicada de testes unitários ficou fora do escopo final devido à priorização das funcionalidades principais dentro do prazo disponível.

## Principais cenários

### Cliente válido

```http
POST /clientes
```

Esperado: `201 Created`.

### Telefone inválido

Esperado: `400 Bad Request`.

### Serviços

```http
GET /servicos
```

Esperado: `200 OK`.

### Criar agendamento com múltiplos serviços

```http
POST /agendamentos
```

Esperado: `201 Created`.

### Conflito de horário

Esperado: `409 Conflict`.

### Regra dos 2 dias

```http
PUT /agendamentos/{id}
```

Para agendamento dentro do limite: `400 Bad Request`.

### Histórico

```http
GET /agendamentos/historico?clienteId={id}&de=DATA_INICIAL&ate=DATA_FINAL
```

Esperado: `200 OK`.

### Login operacional

Acesso direto a `/pages/operacional.html` sem sessão deve redirecionar para login.

Credenciais:

```text
salao.leila
SalaoLeila@0
```

### Agenda operacional

```http
GET /operacional/agendamentos?data=DATA
```

Esperado: `200 OK`.

### Alteração operacional

```http
PUT /operacional/agendamentos/{id}
```

Para horário livre: `200 OK`.

### Comparação da regra de telefone

```text
PUT /agendamentos/{id}
→ 400

PUT /operacional/agendamentos/{id}
→ 200
```

### Conflito operacional

Esperado: `409 Conflict`.

### Status geral

```http
PATCH /operacional/agendamentos/{id}/status
```

Esperado: `200 OK`.

### Status individual

```http
PATCH /operacional/agendamentos/{agendamentoId}/servicos/{itemId}/status
```

Esperado: `200 OK`.

## Consulta de conferência no banco

```sql
SELECT
    a.id AS agendamento_id,
    a.data,
    a.horario,
    a.status AS status_agendamento,
    c.nome AS cliente,
    c.telefone,
    s.nome AS servico,
    s.duracao,
    ags.status AS status_servico
FROM agendamentos a
INNER JOIN clientes c
    ON c.id = a.cliente_id
INNER JOIN agendamento_servicos ags
    ON ags.agendamento_id = a.id
INNER JOIN servicos s
    ON s.id = ags.servico_id
ORDER BY
    a.data,
    a.horario,
    a.id;
```
