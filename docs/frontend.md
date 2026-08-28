# Frontend

## Visão geral

O frontend foi desenvolvido com HTML5, CSS3, JavaScript e Fetch API, sem framework JavaScript.

## Localização

```text
app/src/main/resources/static/
```

Estrutura principal:

```text
static/
├── index.html
├── pages/
│   ├── cliente.html
│   ├── login.html
│   └── operacional.html
├── css/
└── js/
```

## Página inicial

A home apresenta os acessos para Área do Cliente e Painel Operacional.

## Área do cliente

Permite identificar ou cadastrar cliente, carregar serviços, selecionar múltiplos serviços, criar agendamento, apresentar confirmação e consultar histórico.

## Login operacional

O painel da Leila é protegido pelo Spring Security.

Credenciais demonstrativas:

```text
salao.leila
SalaoLeila@0
```

Existe também um fluxo demonstrativo de recuperação de senha por SMS. Nenhuma mensagem real é enviada.

## Painel operacional

Permite selecionar a data, listar atendimentos, visualizar cliente e telefone, visualizar serviços e duração, alterar status geral, alterar status individual, alterar dados do agendamento e efetuar logout.

## Integração

As requisições são centralizadas em `js/api.js`, utilizando `fetch()`.
