# Configuração do Ambiente

## Tecnologias

- Java 17
- Spring Boot
- Maven
- MySQL
- HTML
- CSS
- JavaScript

## Banco de dados

A aplicação utiliza MySQL e recebe as credenciais de conexão por meio de variáveis de ambiente.

Variáveis utilizadas:

- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`

## Execução

A aplicação está localizada no diretório `app`.

```bash
cd app
./mvnw spring-boot:run
```

No Windows:

```bash
cd app
mvnw.cmd spring-boot:run
```

Após a inicialização, o sistema pode ser acessado em:

http://localhost:8080
