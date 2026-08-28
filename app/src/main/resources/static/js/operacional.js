const dataAgendaInput =
    document.getElementById("dataAgenda");

const buscarAgendaButton =
    document.getElementById("buscarAgenda");

const listaAgendamentos =
    document.getElementById("listaAgendamentos");

const totalAgendamentos =
    document.getElementById("totalAgendamentos");

const mensagem =
    document.getElementById("mensagem");

const modalEditarAgendamento =
    document.getElementById(
        "modalEditarAgendamento"
    );

const formEditarAgendamento =
    document.getElementById(
        "formEditarAgendamento"
    );

const editarDataInput =
    document.getElementById(
        "editarData"
    );

const editarHorarioInput =
    document.getElementById(
        "editarHorario"
    );

const editarServicos =
    document.getElementById(
        "editarServicos"
    );

const mensagemEdicao =
    document.getElementById(
        "mensagemEdicao"
    );

const cancelarEdicaoButton =
    document.getElementById(
        "cancelarEdicao"
    );

const fecharEdicaoButton =
    document.getElementById(
        "fecharEdicao"
    );

const salvarEdicaoButton =
    document.getElementById(
        "salvarEdicao"
    );


let agendamentosCarregados = [];

let servicosDisponiveis = [];

let agendamentoEmEdicaoId = null;


function obterDataAtual() {

    const hoje = new Date();

    const ano = hoje.getFullYear();

    const mes = String(
        hoje.getMonth() + 1
    ).padStart(2, "0");

    const dia = String(
        hoje.getDate()
    ).padStart(2, "0");

    return `${ano}-${mes}-${dia}`;
}


function formatarHorario(horario) {

    if (!horario) {
        return "";
    }

    return horario.substring(0, 5);
}


function formatarTelefone(telefone) {

    if (!telefone) {
        return "";
    }

    const numeros =
        telefone.replace(/\D/g, "");

    if (numeros.length === 11) {

        return numeros.replace(
            /(\d{2})(\d{5})(\d{4})/,
            "($1) $2-$3"
        );
    }

    if (numeros.length === 10) {

        return numeros.replace(
            /(\d{2})(\d{4})(\d{4})/,
            "($1) $2-$3"
        );
    }

    return telefone;
}


function textoStatus(status) {

    const textos = {
        AGENDADO: "Agendado",
        CONFIRMADO: "Confirmado",
        EM_ATENDIMENTO: "Em atendimento",
        CONCLUIDO: "Concluído",
        CANCELADO: "Cancelado"
    };

    return textos[status] || status;
}


function classeStatus(status) {

    const classes = {
        AGENDADO: "status-agendado",
        CONFIRMADO: "status-confirmado",
        EM_ATENDIMENTO: "status-em-atendimento",
        CONCLUIDO: "status-concluido",
        CANCELADO: "status-cancelado"
    };

    return classes[status] || "";
}


function criarOpcoesStatusAgendamento(statusAtual) {

    const status = [
        "AGENDADO",
        "CONFIRMADO",
        "CONCLUIDO",
        "CANCELADO"
    ];

    return status
        .map(item => `
            <option
                value="${item}"
                ${item === statusAtual ? "selected" : ""}
            >
                ${textoStatus(item)}
            </option>
        `)
        .join("");
}


function criarOpcoesStatusServico(statusAtual) {

    const status = [
        "AGENDADO",
        "EM_ATENDIMENTO",
        "CONCLUIDO",
        "CANCELADO"
    ];

    return status
        .map(item => `
            <option
                value="${item}"
                ${item === statusAtual ? "selected" : ""}
            >
                ${textoStatus(item)}
            </option>
        `)
        .join("");
}


function criarServicoHtml(
    agendamentoId,
    servico,
    agendamentoCancelado
) {

    return `
        <li class="service-item">

            <div class="service-info">

                <span class="service-name">
                    ${servico.nome}
                </span>

                <span class="service-duration">
                    ${servico.duracao} minutos
                </span>

                <span
                    class="
                        status-badge
                        ${classeStatus(servico.status)}
                    "
                >
                    ${textoStatus(servico.status)}
                </span>

            </div>

            <div class="status-control">

                <label>
                    Status do serviço
                </label>

                <select
                    class="status-select service-status-select"

                    data-agendamento-id="${agendamentoId}"
                    data-item-id="${servico.itemId}"

                    ${agendamentoCancelado
                        ? "disabled"
                        : ""}
                >
                    ${criarOpcoesStatusServico(
                        servico.status
                    )}
                </select>

            </div>

        </li>
    `;
}


function criarAgendamentoHtml(agendamento) {

    const agendamentoCancelado =
        agendamento.status === "CANCELADO";

    const servicosHtml =
        agendamento.servicos
            .map(servico =>
                criarServicoHtml(
                    agendamento.id,
                    servico,
                    agendamentoCancelado
                )
            )
            .join("");

    return `
        <article class="appointment-card">

            <div class="appointment-header">

                <div class="appointment-main">

                    <div class="appointment-time">
                        ${formatarHorario(
                            agendamento.horario
                        )}
                    </div>

                    <div>

                        <div class="client-name">
                            ${agendamento.clienteNome}
                        </div>

                        <div class="client-phone">
                            ${formatarTelefone(
                                agendamento.clienteTelefone
                            )}
                        </div>

                    </div>

                </div>

                <div class="status-control">

                    <label>
                        Status do agendamento
                    </label>

                    <select
                        class="
                            status-select
                            appointment-status-select
                        "

                        data-agendamento-id="${agendamento.id}"
                    >
                        ${criarOpcoesStatusAgendamento(
                            agendamento.status
                        )}
                    </select>

                </div>

            </div>

            <div class="services">

                <h2 class="services-title">
                    Serviços
                </h2>

                <ul class="service-list">
                    ${servicosHtml}
                </ul>

            </div>

            <div class="appointment-actions">

                <button
                    type="button"
                    class="button edit-appointment-button"

                    data-agendamento-id="${agendamento.id}"

                    ${agendamentoCancelado
                        ? "disabled"
                        : ""}
                >
                    Alterar agendamento
                </button>

            </div>

        </article>
    `;
}


function exibirMensagem(
    texto,
    tipo = "error"
) {

    mensagem.innerHTML = `
        <div class="message message-${tipo}">
            ${texto}
        </div>
    `;
}


function limparMensagem() {
    mensagem.innerHTML = "";
}


function renderizarAgendamentos(agendamentos) {

    agendamentosCarregados =
        agendamentos;

    totalAgendamentos.textContent =
        agendamentos.length;

    if (agendamentos.length === 0) {

        listaAgendamentos.innerHTML = `
            <div class="message message-empty">
                Nenhum agendamento encontrado
                para esta data.
            </div>
        `;

        return;
    }

    listaAgendamentos.innerHTML =
        agendamentos
            .map(criarAgendamentoHtml)
            .join("");

    adicionarEventosStatus();
    adicionarEventosEdicao();
}


async function carregarAgendamentos() {

    const data = dataAgendaInput.value;

    buscarAgendaButton.disabled = true;
    limparMensagem();

    try {

        const agendamentos =
            await apiRequest(
                `/operacional/agendamentos?data=${data}`
            );

        renderizarAgendamentos(
            agendamentos
        );

    } catch (error) {

        console.error(error);

        totalAgendamentos.textContent = "0";

        listaAgendamentos.innerHTML = "";

        exibirMensagem(
            "Não foi possível carregar a agenda."
        );

    } finally {

        buscarAgendaButton.disabled = false;
    }
}


async function alterarStatusAgendamento(
    agendamentoId,
    novoStatus
) {

    try {

        await apiRequest(
            `/operacional/agendamentos/${agendamentoId}/status`,
            {
                method: "PATCH",

                body: JSON.stringify({
                    status: novoStatus
                })
            }
        );

        await carregarAgendamentos();

    } catch (error) {

        console.error(error);

        exibirMensagem(
            "Não foi possível atualizar "
            + "o status do agendamento."
        );

        await carregarAgendamentos();
    }
}

async function carregarServicosDisponiveis() {

    if (servicosDisponiveis.length > 0) {
        return;
    }

    servicosDisponiveis =
        await apiRequest(
            "/servicos"
        );
}

function renderizarServicosEdicao(
    agendamento
) {

    const servicosAtuais =
        agendamento.servicos
            .map(servico =>
                servico.servicoId
            );

    editarServicos.innerHTML =
        servicosDisponiveis
            .map(servico => {

                const selecionado =
                    servicosAtuais.includes(
                        servico.id
                    );

                return `
                    <label class="edit-service-option">

                        <input
                            type="checkbox"
                            name="servicoEdicao"
                            value="${servico.id}"

                            ${selecionado
                                ? "checked"
                                : ""}
                        >

                        <span>

                            <strong>
                                ${servico.nome}
                            </strong>

                            <small>
                                ${servico.duracao} minutos
                            </small>

                        </span>

                    </label>
                `;
            })
            .join("");
}

async function abrirEdicao(
    agendamentoId
) {

    limparMensagem();

    const agendamento =
        agendamentosCarregados
            .find(item =>
                item.id === Number(
                    agendamentoId
                )
            );

    if (!agendamento) {

        exibirMensagem(
            "Agendamento não encontrado."
        );

        return;
    }

    try {

        await carregarServicosDisponiveis();

        agendamentoEmEdicaoId =
            agendamento.id;

        editarDataInput.value =
            agendamento.data;

        editarHorarioInput.value =
            formatarHorario(
                agendamento.horario
            );

        mensagemEdicao.innerHTML = "";

        renderizarServicosEdicao(
            agendamento
        );

        modalEditarAgendamento
            .showModal();

    } catch (error) {

        console.error(error);

        exibirMensagem(
            "Não foi possível carregar "
            + "os dados para alteração."
        );
    }
}

function obterServicosEdicao() {

    return Array
        .from(
            document.querySelectorAll(
                'input[name="servicoEdicao"]:checked'
            )
        )
        .map(input =>
            Number(input.value)
        );
}

function exibirMensagemEdicao(
    texto,
    tipo = "error"
) {

    mensagemEdicao.innerHTML = `
        <div class="message message-${tipo}">
            ${texto}
        </div>
    `;
}

async function salvarAlteracaoAgendamento(
    event
) {

    event.preventDefault();

    const data =
        editarDataInput.value;

    const horario =
        editarHorarioInput.value;

    const servicoIds =
        obterServicosEdicao();

    mensagemEdicao.innerHTML = "";

    if (servicoIds.length === 0) {

        exibirMensagemEdicao(
            "Selecione pelo menos um serviço."
        );

        return;
    }

    salvarEdicaoButton.disabled = true;

    try {

        await apiRequest(
            `/operacional/agendamentos/${agendamentoEmEdicaoId}`,
            {
                method: "PUT",

                body: JSON.stringify({
                    data,
                    horario,
                    servicoIds
                })
            }
        );

        modalEditarAgendamento.close();

        agendamentoEmEdicaoId = null;

        await carregarAgendamentos();

        exibirMensagem(
            "Agendamento alterado com sucesso.",
            "success"
        );

    } catch (error) {

        console.error(error);

        if (error.status === 409) {

            exibirMensagemEdicao(
                "O horário informado conflita "
                + "com outro agendamento."
            );

            return;
        }

        if (error.status === 400) {

            exibirMensagemEdicao(
                "Verifique a data, o horário "
                + "e os serviços informados."
            );

            return;
        }

        exibirMensagemEdicao(
            "Não foi possível alterar "
            + "o agendamento."
        );

    } finally {

        salvarEdicaoButton.disabled = false;
    }
}

function adicionarEventosEdicao() {

    const botoes =
        document.querySelectorAll(
            ".edit-appointment-button"
        );

    botoes.forEach(botao => {

        botao.addEventListener(
            "click",
            event => {

                abrirEdicao(
                    event.currentTarget
                        .dataset
                        .agendamentoId
                );
            }
        );
    });
}

async function alterarStatusServico(
    agendamentoId,
    itemId,
    novoStatus
) {

    try {

        await apiRequest(
            `/operacional/agendamentos/${agendamentoId}`
            + `/servicos/${itemId}/status`,
            {
                method: "PATCH",

                body: JSON.stringify({
                    status: novoStatus
                })
            }
        );

        await carregarAgendamentos();

    } catch (error) {

        console.error(error);

        exibirMensagem(
            "Não foi possível atualizar "
            + "o status do serviço."
        );

        await carregarAgendamentos();
    }
}


function adicionarEventosStatus() {

    const statusAgendamentos =
        document.querySelectorAll(
            ".appointment-status-select"
        );

    statusAgendamentos.forEach(select => {

        select.addEventListener(
            "change",
            event => {

                const agendamentoId =
                    event.target.dataset
                        .agendamentoId;

                const novoStatus =
                    event.target.value;

                alterarStatusAgendamento(
                    agendamentoId,
                    novoStatus
                );
            }
        );
    });


    const statusServicos =
        document.querySelectorAll(
            ".service-status-select"
        );

    statusServicos.forEach(select => {

        select.addEventListener(
            "change",
            event => {

                const agendamentoId =
                    event.target.dataset
                        .agendamentoId;

                const itemId =
                    event.target.dataset.itemId;

                const novoStatus =
                    event.target.value;

                alterarStatusServico(
                    agendamentoId,
                    itemId,
                    novoStatus
                );
            }
        );
    });
}


buscarAgendaButton.addEventListener(
    "click",
    carregarAgendamentos
);


dataAgendaInput.addEventListener(
    "change",
    carregarAgendamentos
);

cancelarEdicaoButton.addEventListener(
    "click",
    () => {

        modalEditarAgendamento.close();

        agendamentoEmEdicaoId = null;
    }
);


fecharEdicaoButton.addEventListener(
    "click",
    () => {

        modalEditarAgendamento.close();

        agendamentoEmEdicaoId = null;
    }
);


formEditarAgendamento.addEventListener(
    "submit",
    salvarAlteracaoAgendamento
);

dataAgendaInput.value =
    obterDataAtual();

carregarAgendamentos();
