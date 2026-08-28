# Salão Leila

Sistema de agendamento desenvolvido para o teste técnico da DSIN, com foco no atendimento das necessidades do salão **Cabeleleila Leila**.

A aplicação permite o cadastro e consulta de clientes e serviços, criação de agendamentos com um ou mais serviços, consulta de histórico e operações administrativas para gerenciamento da agenda.

## Evidências

Os prints da aplicação são organizados em:

[Prints](docs/evidencias/prints/)

O vídeo de demonstração é referenciado em:

[README do Vídeo](docs/evidencias/video/README.md)
[Assistir ao vídeo de demonstração](https://drive.google.com/file/d/1hW8tDxwKj6asrLO7SnzDtHi6zFe-9Gkf/view?usp=sharing)

## Funcionalidades

### Área do cliente

- identificação do cliente por telefone;
- cadastro de novo cliente;
- consulta dos serviços disponíveis;
- agendamento de um ou mais serviços;
- validação de disponibilidade considerando a duração total dos serviços;
- prevenção de conflito entre horários;
- validação de dados de entrada;
- histórico de agendamentos por período;
- bloqueio de alteração quando o agendamento estiver a menos de 2 dias, direcionando a solicitação para atendimento por telefone.

### Área operacional

- acesso restrito ao painel operacional;
- autenticação utilizando Spring Security;
- listagem da agenda por data;
- visualização de cliente, telefone e serviços;
- alteração do status geral do agendamento;
- gerenciamento do status individual de cada serviço;
- alteração operacional de data, horário e serviços;
- possibilidade de registrar alterações solicitadas por telefone mesmo dentro do prazo inferior a 2 dias;
- logout;
- fluxo demonstrativo de recuperação de senha.

## Credenciais de demonstração

Para facilitar a avaliação do painel operacional:

```text
Usuário: salao.leila
Senha: SalaoLeila@0
```

As credenciais são exclusivas para demonstração do teste técnico. Em um cenário de produção, usuários e credenciais seriam gerenciados de forma persistente e segura.

## Tecnologias

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- Hibernate
- Spring Security
- Bean Validation
- Maven
- MySQL
- Aiven
- HTML5
- CSS3
- JavaScript
- Fetch API
- Git e GitHub
- Postman
- MySQL Workbench

## Arquitetura

A aplicação utiliza arquitetura em camadas:

```text
Frontend 
(HTML/CSS/JavaScript)
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

O frontend é servido pelo próprio Spring Boot a partir de `src/main/resources/static`, porém a comunicação com as funcionalidades da aplicação ocorre por meio da API HTTP.

A organização principal do backend é:

```text
controller -> service -> repository -> model
                  |
                 DTOs
```

Mais detalhes em [Arquitetura](docs/arquitetura.md).

## Estrutura do repositório

```text
salao-leila/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   └── resources/
│   │   │       └── static/
│   │   └── test/
│   ├── pom.xml
│   ├── mvnw
│   └── mvnw.cmd
├── docs/
│   ├── README.md
│   ├── arquitetura.md
│   ├── configuracao-ambiente.md
│   ├── modelo-dados.md
│   ├── api.md
│   ├── regras-negocio.md
│   ├── frontend.md
│   ├── testes.md
│   ├── decisoes-tecnicas.md
│   └── evidencias/
├── sql/
│   ├── README.md
│   └── database.sql
├── .gitignore
└── README.md
```

## Como executar

### Pré-requisitos

- Java 17
- acesso a um servidor MySQL
- Maven ou Maven Wrapper
- navegador web

Opcionalmente:

- IntelliJ IDEA
- MySQL Workbench
- Postman

### Banco de dados

Durante o desenvolvimento foi utilizado um servidor MySQL hospedado no **Aiven**.

A aplicação recebe as credenciais por variáveis de ambiente:

```text
DB_URL
DB_USERNAME
DB_PASSWORD
```

Exemplo de URL JDBC:

```text
jdbc:mysql://HOST:PORT/defaultdb?sslMode=REQUIRED
```

Não foi armazenado credenciais reais no repositório.

### Linux/macOS

```bash
cd app
chmod +x mvnw
./mvnw spring-boot:run
```

O `chmod` só é necessário caso o Maven Wrapper não esteja com permissão de execução.

### Windows

```bat
cd app
mvnw.cmd spring-boot:run
```

Após a inicialização:

```text
http://localhost:8080/
```

## Interfaces

- Home: `http://localhost:8080/`
- Área do cliente: `http://localhost:8080/pages/cliente.html`
- Painel operacional: `http://localhost:8080/pages/operacional.html`
- Login operacional: `http://localhost:8080/pages/login.html`

O acesso direto ao painel operacional redireciona o usuário não autenticado para a tela de login.

## API

A documentação dos endpoints está disponível em:

[Documentação da API](docs/api.md)

## Banco de dados

A modelagem e as consultas de verificação estão disponíveis em:

[Modelo de dados](docs/modelo-dados.md)

Também é disponibilizado:

[Script SQL](sql/database.sql)

Durante o desenvolvimento, a estrutura foi criada e atualizada pelo Hibernate/JPA utilizando:

```properties
spring.jpa.hibernate.ddl-auto=update
```

O script SQL é fornecido como documentação e alternativa de criação manual da estrutura.

## Testes

A validação da entrega foi planejada com:

- testes manuais da API utilizando Postman;
- testes de integração pelo navegador;
- conferência de persistência pelo MySQL Workbench;
- validação de regras de negócio e respostas HTTP.

Os cenários estão documentados em:

[Testes realizados e cenários](docs/testes.md)

## Documentação técnica

A documentação completa está em:

[Índice da documentação](docs/README.md)

Arquivos principais:

- [Configuração do ambiente](docs/configuracao-ambiente.md)
- [Arquitetura](docs/arquitetura.md)
- [Modelo de dados](docs/modelo-dados.md)
- [API](docs/api.md)
- [Regras de negócio](docs/regras-negocio.md)
- [Frontend](docs/frontend.md)
- [Testes](docs/testes.md)
- [Decisões técnicas](docs/decisoes-tecnicas.md)

## Decisões e limitações da entrega

A aplicação foi desenvolvida como um único projeto Maven para simplificar execução e avaliação. O frontend é estático e servido pelo Spring Boot, enquanto as regras de negócio permanecem concentradas no backend.

A autenticação operacional utiliza um usuário em memória com Spring Security, suficiente para o objetivo demonstrativo do teste. Em produção, credenciais seriam persistidas e externalizadas.

O envio de SMS do fluxo "Esqueci minha senha" é apenas demonstrativo e não utiliza provedor externo.

O dashboard gerencial semanal e uma issue dedicada de testes unitários não fazem parte do escopo final desta versão. O tempo disponível foi priorizado para as funcionalidades de agendamento, regras de negócio, fluxo operacional, validações, documentação e testes manuais de integração.

## Possíveis evoluções

- testes unitários e de integração automatizados;
- dashboard gerencial semanal;
- autenticação persistida;
- recuperação real de senha por e-mail ou SMS;
- migrations com Flyway ou Liquibase;
- Docker e Docker Compose para padronização do ambiente;
- deploy da aplicação;
- auditoria de alterações de agendamento.

---

Projeto desenvolvido como parte do teste técnico para a vaga de Desenvolvimento de Sistemas.
