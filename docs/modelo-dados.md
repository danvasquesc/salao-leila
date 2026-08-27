# Modelo de Dados

## Visão Geral

O sistema Salão Leila utiliza MySQL como banco de dados relacional.

Durante o desenvolvimento foi criado um servidor MySQL remoto na plataforma Aiven, utilizado tanto pela aplicação Spring Boot quanto pelo MySQL Workbench.

A estrutura das tabelas foi modelada por meio das entidades JPA da aplicação e criada/atualizada automaticamente pelo Hibernate.

A configuração utilizada durante o desenvolvimento é:

```properties
spring.jpa.hibernate.ddl-auto=update
```

Dessa forma, as entidades Java representam o modelo principal da aplicação e o Hibernate realiza o mapeamento objeto-relacional e a atualização da estrutura no banco MySQL.

Como parte da documentação do projeto também é disponibilizado o arquivo:

[`../sql/database.sql`](../sql/database.sql)

Esse script representa a estrutura relacional utilizada pela aplicação e pode ser utilizado como referência ou para criação manual das tabelas.

---

## Ambiente do Banco de Dados

Durante o desenvolvimento foi criada uma instância MySQL hospedada na plataforma Aiven. A utilização de um banco remoto permite que os dados permaneçam disponíveis independentemente da máquina utilizada para executar a aplicação.

A instância configurada utiliza:

- MySQL;
- banco de dados `defaultdb`;
- conexão remota;
- autenticação por usuário e senha;
- conexão SSL obrigatória;
- host e porta fornecidos pelo Aiven.

As informações de conexão são disponibilizadas diretamente pelo painel da plataforma Aiven.

Por segurança, credenciais sensíveis, principalmente a senha do banco de dados, **não são armazenadas neste repositório**.

---

## Conexão da Aplicação com o Banco

A aplicação Spring Boot se conecta ao servidor MySQL utilizando as configurações presentes no arquivo:

`app/src/main/resources/application.properties`

A configuração utilizada é:

```properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

As credenciais são fornecidas por meio das seguintes variáveis de ambiente:

- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`

Essa abordagem evita que credenciais de acesso ao banco sejam armazenadas diretamente no código-fonte ou enviadas para o repositório Git.

---

## Acesso pelo MySQL Workbench

Durante o desenvolvimento, o MySQL Workbench foi utilizado para acessar diretamente o mesmo banco de dados hospedado no Aiven. A conexão foi configurada utilizando os dados fornecidos pelo painel da plataforma:

- Host;
- Port;
- Username;
- Password;
- Database;
- SSL Mode.

O serviço utilizado no projeto exige conexão SSL:

```text
SSL Mode: REQUIRED
```

Após a configuração, o MySQL Workbench foi utilizado para:

- verificar a criação das tabelas;
- consultar registros;
- validar relacionamentos;
- verificar dados cadastrados pela aplicação;
- acompanhar alterações realizadas pela API;
- executar consultas de teste;
- validar status de agendamentos;
- validar status individual dos serviços;
- conferir persistência dos dados.

---

## Criação das Tabelas

Durante o desenvolvimento, as tabelas não foram inicialmente criadas por scripts SQL manuais. A estrutura foi gerenciada pelo Hibernate/JPA, com base nas entidades Java da aplicação.

A propriedade:

```properties
spring.jpa.hibernate.ddl-auto=update
```

permite que o Hibernate crie ou atualize automaticamente a estrutura necessária no banco de dados durante o desenvolvimento.

O arquivo:

[`../sql/database.sql`](../sql/database.sql)

é disponibilizado como parte da documentação técnica do projeto. Ele representa a estrutura relacional utilizada pela aplicação e também pode ser utilizado como referência para criação manual do banco.

---

# Entidades

## Cliente

A entidade `Cliente` representa uma pessoa que utiliza os serviços do salão.

Tabela:

```text
clientes
```

Principais campos:

| Campo     | Tipo    | Descrição                                       |
|-----------|---------|-------------------------------------------------|
| `id`      | BIGINT  | Identificador único do cliente                  |
| `nome`    | VARCHAR | Nome do cliente                                 |
| `telefone`| VARCHAR | Telefone utilizado para contato e identificação |
| `email`   | VARCHAR | E-mail do cliente                               |

Um cliente pode possuir vários agendamentos.

---

## Serviço

A entidade `Servico` representa um serviço oferecido pelo salão.

Tabela:

```text
servicos
```

Principais campos:

| Campo      | Tipo    | Descrição                   |
|------------|---------|-----------------------------|
| `id`       | BIGINT  | Identificador único         |
| `nome`     | VARCHAR | Nome do serviço             |
| `descricao`| VARCHAR | Descrição do serviço        |
| `preco`    | DECIMAL | Valor cobrado pelo serviço  |
| `duracao`  | INTEGER | Duração estimada em minutos |

Exemplos de serviços:

- Corte;
- Escova;
- Coloração;
- Manicure;
- Pedicure.

---

## Agendamento

A entidade `Agendamento` representa uma reserva de horário realizada por um cliente.

Tabela:

```text
agendamentos
```

Principais campos:

| Campo       | Tipo    | Descrição                           |
|-------------|---------|-------------------------------------|
| `id`        | BIGINT  | Identificador do agendamento        |
| `data`      | DATE    | Data do atendimento                 |
| `horario`   | TIME    | Horário inicial                     |
| `status`    | VARCHAR | Status atual do agendamento         |
| `cliente_id`| BIGINT  | Cliente responsável pelo agendamento|

O relacionamento com o cliente é realizado através da chave estrangeira:

```text
cliente_id
```

Um cliente pode possuir vários agendamentos.

---

## Status do Agendamento

O status geral do agendamento é armazenado utilizando enumeração Java e persistido como texto no banco.

Os estados disponíveis são:

```text
AGENDADO
CONFIRMADO
CONCLUIDO
CANCELADO
```

O armazenamento como texto é realizado através do JPA:

```java
@Enumerated(EnumType.STRING)
```

Essa abordagem facilita a leitura dos dados diretamente no banco e evita problemas causados pela alteração da ordem dos valores de um enum.

---

## AgendamentoServico

A entidade `AgendamentoServico` representa a associação entre um agendamento e os serviços solicitados pelo cliente.

Tabela:

```text
agendamento_servicos
```

Principais campos:

| Campo           | Tipo    | Descrição                                    |
|-----------------|---------|----------------------------------------------|
| `id`            | BIGINT  | Identificador do item                        |
| `agendamento_id`| BIGINT  | Agendamento relacionado                      |
| `servico_id`    | BIGINT  | Serviço solicitado                           |
| `status`        | VARCHAR | Status daquele serviço dentro do atendimento |


Essa entidade foi criada porque um agendamento pode possuir **um ou mais serviços**. Além disso, o painel operacional precisa controlar individualmente o status de cada serviço solicitado.

---

## Por que foi utilizada uma entidade associativa?

Uma alternativa simples seria utilizar um relacionamento:

```java
@ManyToMany
```

diretamente entre `Agendamento` e `Servico`. Porém, nesse caso a tabela intermediária teria apenas os identificadores das duas entidades. O requisito operacional exige que cada serviço solicitado pelo cliente possua seu próprio status. Por isso foi criada explicitamente a entidade:

```text
AgendamentoServico
```

Assim é possível representar situações como:

```text
Agendamento: CONFIRMADO

Escova:
CONCLUIDO

Manicure:
EM_ATENDIMENTO
```

Essa modelagem também permite que futuramente sejam adicionadas novas informações específicas do serviço dentro de um atendimento, como:

- horário de início;
- horário de conclusão;
- profissional responsável;
- observações;
- preço praticado.

---

## Status dos Serviços do Agendamento

Cada registro da tabela:

```text
agendamento_servicos
```

possui seu próprio status operacional.

Os estados disponíveis são:

```text
AGENDADO
EM_ATENDIMENTO
CONCLUIDO
CANCELADO
```

Quando um serviço é adicionado a um novo agendamento, seu status inicial é:

```text
AGENDADO
```

Quando o agendamento inteiro é cancelado, os serviços associados também recebem o status:

```text
CANCELADO
```

---

# Relacionamentos

O modelo possui os seguintes relacionamentos:

```text
Cliente 1:N Agendamento
```

Um cliente pode possuir vários agendamentos.

```text
Agendamento 1:N AgendamentoServico
```

Um agendamento pode possuir vários serviços.

```text
Servico 1:N AgendamentoServico
```

Um mesmo serviço pode estar presente em diferentes agendamentos.

Representação simplificada:

```text
Cliente
   |
   | 1
   |
   | N
Agendamento
   |
   | 1
   |
   | N
AgendamentoServico
   |
   | N
   |
   | 1
Servico
```

---

# Integridade dos Dados

A tabela:

```text
agendamento_servicos
```

possui uma restrição de unicidade para:

```text
agendamento_id + servico_id
```

Isso impede que o mesmo serviço seja associado duas vezes ao mesmo agendamento.

Conceitualmente:

```sql
UNIQUE (agendamento_id, servico_id)
```

Exemplo permitido:

```text
Agendamento 10
- Escova
- Manicure
```

Exemplo não permitido:

```text
Agendamento 10
- Escova
- Escova
```

---

# Consultas de Verificação

As consultas abaixo podem ser executadas no MySQL Workbench para apresentar a verificação dos dados durante os testes e a demonstração do sistema.

## Listar Clientes

```sql
SELECT *
FROM clientes
ORDER BY id;
```

---

## Listar Serviços

```sql
SELECT *
FROM servicos
ORDER BY id;
```

---

## Listar Agendamentos

```sql
SELECT
    id,
    data,
    horario,
    status,
    cliente_id
FROM agendamentos
ORDER BY
    data,
    horario;
```

---

## Listar Serviços dos Agendamentos

```sql
SELECT
    id,
    agendamento_id,
    servico_id,
    status
FROM agendamento_servicos
ORDER BY
    agendamento_id,
    id;
```

---

## Visualizar Agendamentos com Clientes

```sql
SELECT
    a.id AS agendamento_id,
    a.data,
    a.horario,
    a.status,
    c.id AS cliente_id,
    c.nome AS cliente,
    c.telefone
FROM agendamentos a
INNER JOIN clientes c
    ON c.id = a.cliente_id
ORDER BY
    a.data,
    a.horario;
```

---

## Visualização Completa dos Agendamentos

A consulta abaixo apresenta em conjunto:

- agendamento;
- data;
- horário;
- cliente;
- serviço;
- status do agendamento;
- status individual do serviço.

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

Essa consulta é especialmente útil para demonstrar que os dados cadastrados e alterados pela aplicação estão sendo persistidos corretamente no banco de dados.

---

## Histórico de um Cliente

Para consultar os agendamentos de um cliente específico:

```sql
SELECT
    a.id,
    a.data,
    a.horario,
    a.status,
    c.nome AS cliente
FROM agendamentos a
INNER JOIN clientes c
    ON c.id = a.cliente_id
WHERE c.id = 4
ORDER BY
    a.data DESC,
    a.horario DESC;
```

O identificador utilizado na cláusula pode ser substituído pelo cliente desejado:

```sql
WHERE c.id = 4
```

---

## Histórico por Período

```sql
SELECT
    a.id,
    a.data,
    a.horario,
    a.status,
    c.nome AS cliente
FROM agendamentos a
INNER JOIN clientes c
    ON c.id = a.cliente_id
WHERE
    c.id = 4
    AND a.data BETWEEN '2026-08-01' AND '2026-08-31'
ORDER BY
    a.data DESC,
    a.horario DESC;
```

As datas e o identificador do cliente podem ser alterados conforme o cenário de teste.

---

## Agenda de um Dia

Consulta útil para validar o painel operacional:

```sql
SELECT
    a.id AS agendamento_id,
    a.horario,
    c.nome AS cliente,
    a.status
FROM agendamentos a
INNER JOIN clientes c
    ON c.id = a.cliente_id
WHERE a.data = '2026-08-27'
ORDER BY a.horario;
```

A data pode ser substituída pela data utilizada durante a demonstração.

---

## Status Individual dos Serviços

```sql
SELECT
    a.id AS agendamento_id,
    c.nome AS cliente,
    s.nome AS servico,
    ags.status AS status_servico
FROM agendamento_servicos ags
INNER JOIN agendamentos a
    ON a.id = ags.agendamento_id
INNER JOIN clientes c
    ON c.id = a.cliente_id
INNER JOIN servicos s
    ON s.id = ags.servico_id
ORDER BY
    a.id,
    ags.id;
```

Essa consulta permite verificar que serviços pertencentes ao mesmo agendamento podem possuir status diferentes.

---

# Verificação da Estrutura das Tabelas

Também é possível verificar diretamente a estrutura criada pelo Hibernate através dos comandos:

```sql
SHOW CREATE TABLE clientes;
```

```sql
SHOW CREATE TABLE servicos;
```

```sql
SHOW CREATE TABLE agendamentos;
```

```sql
SHOW CREATE TABLE agendamento_servicos;
```

Esses comandos permitem visualizar:

- tipos das colunas;
- chaves primárias;
- chaves estrangeiras;
- índices;
- restrições de unicidade.

---

# Script SQL

Como complemento à estrutura criada automaticamente pelo Hibernate/JPA, o projeto disponibiliza:

[`../sql/database.sql`](../sql/database.sql)

O arquivo contém a representação SQL das principais tabelas utilizadas pela aplicação.

---

# Considerações

A utilização de JPA/Hibernate permitiu manter o modelo relacional diretamente relacionado às entidades do domínio da aplicação.

Para este projeto, a configuração foi utilizada por ser adequada ao ambiente de desenvolvimento e ao escopo do teste técnico:

```properties
spring.jpa.hibernate.ddl-auto=update
```
