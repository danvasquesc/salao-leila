# Decisões Técnicas

## Projeto único Maven

Backend e frontend foram mantidos no mesmo projeto Maven para simplificar execução e avaliação.

## Arquitetura em camadas

Foi adotada a separação Controller, Service, Repository, Model e DTO.

## DTOs

Entidades JPA não são utilizadas diretamente como contrato da API.

## Entidade AgendamentoServico

Foi criada uma entidade associativa porque cada serviço solicitado precisa possuir status próprio.

## Hibernate/JPA

Durante o desenvolvimento foi utilizado:

```properties
spring.jpa.hibernate.ddl-auto=update
```

Em produção seria preferível utilizar migrations.

## Aiven

O banco MySQL foi hospedado remotamente no Aiven. Credenciais são fornecidas por variáveis de ambiente.

## Spring Security

O painel operacional utiliza autenticação por sessão e usuário em memória para demonstração.

## Recuperação de senha

O fluxo de SMS é demonstrativo e não integra com provedor externo.

## Frontend sem framework

HTML, CSS e JavaScript puro foram suficientes para o escopo e evitaram um processo de build adicional.

## Testes

A prioridade foi validar os fluxos principais por Postman, navegador e MySQL Workbench.

## Docker

Não foi utilizado nesta entrega. Docker Compose é uma evolução possível para padronização do ambiente.

## Dashboard gerencial

O painel gerencial semanal não foi priorizado em relação ao fluxo principal e operacional.
