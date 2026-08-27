-- =========================================================
-- Script de estrutura do banco de dados
-- =========================================================
-- Durante o desenvolvimento, a estrutura das tabelas foi
-- criada e atualizada pelo Hibernate/JPA por meio das
-- entidades da aplicação.
--
-- Este script é disponibilizado como documentação da
-- estrutura relacional e como alternativa para criação
-- manual das tabelas.
-- =========================================================

-- ---------------------------------------------------------
-- CLIENTES
-- ---------------------------------------------------------

CREATE TABLE IF NOT EXISTS clientes (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    telefone VARCHAR(20) NOT NULL,
    email VARCHAR(100),

    PRIMARY KEY (id)
);

-- ---------------------------------------------------------
-- SERVIÇOS
-- ---------------------------------------------------------

CREATE TABLE IF NOT EXISTS servicos (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    descricao VARCHAR(255),
    preco DECIMAL(10, 2) NOT NULL,
    duracao INT NOT NULL,

    PRIMARY KEY (id)
);

-- ---------------------------------------------------------
-- AGENDAMENTOS
-- ---------------------------------------------------------

CREATE TABLE IF NOT EXISTS agendamentos (
    id BIGINT NOT NULL AUTO_INCREMENT,
    data DATE NOT NULL,
    horario TIME NOT NULL,
    status VARCHAR(20) NOT NULL,
    cliente_id BIGINT NOT NULL,

    PRIMARY KEY (id),

    CONSTRAINT fk_agendamento_cliente
        FOREIGN KEY (cliente_id)
        REFERENCES clientes(id)
);

-- ---------------------------------------------------------
-- SERVIÇOS DO AGENDAMENTO
-- ---------------------------------------------------------

CREATE TABLE IF NOT EXISTS agendamento_servicos (
    id BIGINT NOT NULL AUTO_INCREMENT,
    agendamento_id BIGINT NOT NULL,
    servico_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'AGENDADO',

    PRIMARY KEY (id),

    CONSTRAINT uk_agendamento_servico
        UNIQUE (agendamento_id, servico_id),

    CONSTRAINT fk_agendamento_servico_agendamento
        FOREIGN KEY (agendamento_id)
        REFERENCES agendamentos(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_agendamento_servico_servico
        FOREIGN KEY (servico_id)
        REFERENCES servicos(id)
);

-- =========================================================
-- CONSULTAS BÁSICAS DE VERIFICAÇÃO
-- =========================================================

SELECT * FROM clientes;

SELECT * FROM servicos;

SELECT * FROM agendamentos
ORDER BY data, horario;

SELECT *
FROM agendamento_servicos
ORDER BY agendamento_id, id;
