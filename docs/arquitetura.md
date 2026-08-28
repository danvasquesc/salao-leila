# Arquitetura

## Visão geral

O sistema foi desenvolvido utilizando uma arquitetura em camadas.

```text
Frontend
   |
   | HTTP / JSON
   v
Controller
   |
   v
Service
   |
   v
Repository
   |
   v
MySQL
```

## Camadas

### Controller

Responsável por receber requisições HTTP, validar DTOs de entrada, encaminhar operações para a camada de serviço e retornar respostas HTTP.

Pacote:

```text
br.com.dsin.salaoleila.controller
```

### Service

Concentra as regras de negócio, incluindo criação e alteração de agendamentos, prazo de alteração, disponibilidade, duração total, histórico e operações do painel da Leila.

Pacote:

```text
br.com.dsin.salaoleila.service
```

### Repository

Utiliza Spring Data JPA para acesso ao banco.

Pacote:

```text
br.com.dsin.salaoleila.repository
```

### Model

Contém as entidades JPA e enums:

```text
Cliente
Servico
Agendamento
AgendamentoServico
```

### DTO

Requests e responses específicos evitam expor diretamente as entidades JPA pela API.

## Frontend

O frontend utiliza HTML, CSS, JavaScript e Fetch API, em:

```text
app/src/main/resources/static/
```

Apesar de ser servido pelo próprio Spring Boot, o frontend consome a aplicação por requisições HTTP.

## Segurança

O painel operacional utiliza Spring Security com autenticação por sessão.

As rotas:

```text
/pages/operacional.html
/operacional/**
```

são restritas ao papel operacional.

A credencial é mantida em memória apenas para demonstração.

## Persistência

O banco utilizado é MySQL e o mapeamento é feito com JPA/Hibernate.

```text
Cliente 1:N Agendamento
Agendamento 1:N AgendamentoServico
Servico 1:N AgendamentoServico
```

A entidade associativa `AgendamentoServico` foi adotada porque cada serviço solicitado precisa possuir status próprio.
