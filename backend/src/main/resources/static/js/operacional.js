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


dataAgendaInput.value =
    obterDataAtual();

carregarAgendamentos();
