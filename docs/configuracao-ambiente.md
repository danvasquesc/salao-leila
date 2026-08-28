# Configuração do Ambiente

## Pré-requisitos

Para executar o projeto são necessários:

- Java 17;
- Maven ou Maven Wrapper;
- acesso a um servidor MySQL;
- navegador web.

Ferramentas utilizadas durante o desenvolvimento:

- IntelliJ IDEA;
- MySQL Workbench;
- Postman;
- Git;
- GitHub.

## Estrutura da aplicação

O projeto Maven está em:

```text
app/
```

O frontend não exige servidor separado. Os arquivos HTML, CSS e JavaScript estão em:

```text
app/src/main/resources/static/
```

e são servidos pelo Spring Boot.

## Banco de dados

Durante o desenvolvimento foi criada uma instância MySQL na plataforma **Aiven**.

O mesmo banco foi acessado:

- pela aplicação Spring Boot;
- pelo MySQL Workbench;
- durante os testes da API e da interface.

A instância utiliza conexão SSL obrigatória.

### Configuração no MySQL Workbench

Crie uma nova conexão MySQL utilizando os dados disponíveis no painel do Aiven:

```text
Hostname: <host fornecido pelo Aiven>
Port: <porta fornecida pelo Aiven>
Username: <usuário fornecido pelo Aiven>
Password: <senha fornecida pelo Aiven>
Default Schema: defaultdb
SSL Mode: REQUIRED
```

As credenciais não devem ser versionadas no Git.

## Variáveis de ambiente

O arquivo:

```text
app/src/main/resources/application.properties
```

utiliza:

```properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.open-in-view=false
spring.jpa.properties.hibernate.format_sql=true
```

Configure:

```text
DB_URL
DB_USERNAME
DB_PASSWORD
```

Exemplo de URL:

```text
jdbc:mysql://HOST:PORT/defaultdb?sslMode=REQUIRED
```

No IntelliJ IDEA, as variáveis foram adicionadas em:

```text
Run
→ Edit Configurations
→ Environment variables
```

Exemplo de formato:

```text
DB_URL=jdbc:mysql://HOST:PORT/defaultdb?sslMode=REQUIRED;DB_USERNAME=USUARIO;DB_PASSWORD=SENHA
```

## Execução no Linux/macOS

Na raiz do repositório:

```bash
cd app
chmod +x mvnw
./mvnw spring-boot:run
```

O `chmod` é necessário apenas se o arquivo `mvnw` não possuir permissão de execução.

## Execução no Windows

```bat
cd app
mvnw.cmd spring-boot:run
```

## Acesso

Com a aplicação iniciada:

```text
http://localhost:8080/
```

Principais telas:

```text
http://localhost:8080/pages/cliente.html
http://localhost:8080/pages/login.html
http://localhost:8080/pages/operacional.html
```

## Acesso operacional

Credenciais demonstrativas:

```text
Usuário: salao.leila
Senha: SalaoLeila@0
```

O painel operacional é protegido pelo Spring Security.

## Observação sobre Docker

Docker e Docker Compose não foram adicionados nesta entrega para evitar complexidade desnecessária dentro do prazo do teste.

Como evolução, a aplicação e o banco poderiam ser containerizados para reduzir diferenças de ambiente e simplificar a execução em outras máquinas.
