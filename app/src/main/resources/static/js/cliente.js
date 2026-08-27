const listaServicosCliente =
    document.getElementById("listaServicosCliente");

const mensagemCliente =
    document.getElementById("mensagemCliente");

const formAgendamento =
    document.getElementById(
        "formAgendamento"
    );

const confirmarAgendamentoButton =
    document.getElementById(
        "confirmarAgendamento"
    );

const clienteTelefoneInput =
    document.getElementById("clienteTelefone");


function formatarMoeda(valor) {

    return Number(valor).toLocaleString(
        "pt-BR",
        {
            style: "currency",
            currency: "BRL"
        }
    );
}


function criarServicoHtml(servico) {

    return `
        <label class="service-option">

            <input
                type="checkbox"
                name="servicos"
                value="${servico.id}"
            >

            <div class="service-option-content">

                <span class="service-option-name">
                    ${servico.nome}
                </span>

                <span class="service-option-description">
                    ${servico.descricao || "Sem descrição"}
                </span>

                <span class="service-option-details">
                    ${formatarMoeda(servico.preco)}
                    ·
                    ${servico.duracao} minutos
                </span>

            </div>

        </label>
    `;
}


function exibirMensagemCliente(
    texto,
    tipo = "error"
) {

    mensagemCliente.innerHTML = `
        <div class="message message-${tipo}">
            ${texto}
        </div>
    `;
}


function limparMensagemCliente() {
    mensagemCliente.innerHTML = "";
}


function renderizarServicos(servicos) {

    if (servicos.length === 0) {

        listaServicosCliente.innerHTML = `
            <div class="message message-empty">
                Nenhum serviço disponível no momento.
            </div>
        `;

        return;
    }

    listaServicosCliente.innerHTML =
        servicos
            .map(criarServicoHtml)
            .join("");
}


async function carregarServicos() {

    limparMensagemCliente();

    try {

        const servicos =
            await apiRequest("/servicos");

        renderizarServicos(servicos);

    } catch (error) {

        console.error(error);

        listaServicosCliente.innerHTML = "";

        exibirMensagemCliente(
            "Não foi possível carregar os serviços."
        );
    }
}


function normalizarTelefone(telefone) {

    return telefone.replace(/\D/g, "");
}


async function buscarClientePorTelefone(
    telefone
) {

    try {

        return await apiRequest(
            `/clientes/buscar?telefone=${
                encodeURIComponent(telefone)
            }`
        );

    } catch (error) {

        if (error.status === 404) {
            return null;
        }

        throw error;
    }
}


async function criarCliente(
    nome,
    telefone,
    email
) {

    return apiRequest(
        "/clientes",
        {
            method: "POST",

            body: JSON.stringify({
                nome: nome,
                telefone: telefone,
                email: email || null
            })
        }
    );
}

async function obterOuCriarCliente() {

    const nome =
        document
            .getElementById("clienteNome")
            .value
            .trim();

    const telefoneInformado =
        document
            .getElementById("clienteTelefone")
            .value
            .trim();

    const email =
        document
            .getElementById("clienteEmail")
            .value
            .trim();

    const telefone =
        normalizarTelefone(
            telefoneInformado
        );

        if (
            telefone.length !== 10
            && telefone.length !== 11
        ) {

            throw new Error(
                "Telefone inválido."
            );
        }

    const clienteExistente =
        await buscarClientePorTelefone(
            telefone
        );

    if (clienteExistente) {
        return clienteExistente;
    }

    return criarCliente(
        nome,
        telefone,
        email
    );
}


function obterServicosSelecionados() {

    return Array.from(
        document.querySelectorAll(
            'input[name="servicos"]:checked'
        )
    ).map(input =>
        Number(input.value)
    );
}


async function criarAgendamento(
    clienteId,
    servicoIds,
    data,
    horario
) {

    return apiRequest(
        "/agendamentos",
        {
            method: "POST",

            body: JSON.stringify({
                data: data,
                horario: horario,
                clienteId: clienteId,
                servicoIds: servicoIds
            })
        }
    );
}


function formatarData(data) {

    if (!data) {
        return "";
    }

    const partes =
        data.split("-");

    return `${partes[2]}/${partes[1]}/${partes[0]}`;
}


function exibirConfirmacaoAgendamento(
    agendamento
) {

    const confirmacao =
        document.getElementById(
            "confirmacaoAgendamento"
        );

    const nomesServicos =
        agendamento.servicos
            .map(servico => servico.nome)
            .join(", ");

    confirmacao.innerHTML = `
        <h2>
            Agendamento confirmado
        </h2>

        <p>
            Seu agendamento foi realizado
            com sucesso.
        </p>

        <p>
            <strong>Data:</strong>
            ${formatarData(agendamento.data)}
        </p>

        <p>
            <strong>Horário:</strong>
            ${agendamento.horario.substring(0, 5)}
        </p>

        <p>
            <strong>Serviços:</strong>
            ${nomesServicos}
        </p>

        <p>
            <strong>Status:</strong>
            Agendado
        </p>
    `;

    confirmacao.hidden = false;
}


function limparDadosAgendamento() {

    document
        .querySelectorAll(
            'input[name="servicos"]:checked'
        )
        .forEach(input => {
            input.checked = false;
        });

    document.getElementById(
        "agendamentoData"
    ).value = "";

    document.getElementById(
        "agendamentoHorario"
    ).value = "";
}


clienteTelefoneInput.addEventListener(
    "input",
    event => {

        event.target.value =
            event.target.value
                .replace(/\D/g, "")
                .slice(0, 11);
    }
);


formAgendamento.addEventListener(
    "submit",
    async event => {

        event.preventDefault();

        limparMensagemCliente();

        const servicoIds =
            obterServicosSelecionados();

        if (servicoIds.length === 0) {

            exibirMensagemCliente(
                "Selecione pelo menos um serviço."
            );

            return;
        }

        const data =
            document.getElementById(
                "agendamentoData"
            ).value;

        const horario =
            document.getElementById(
                "agendamentoHorario"
            ).value;

        confirmarAgendamentoButton.disabled =
            true;

        try {

            const cliente =
                await obterOuCriarCliente();

            const agendamento =
                await criarAgendamento(
                    cliente.id,
                    servicoIds,
                    data,
                    horario
                );

            exibirConfirmacaoAgendamento(
                agendamento
            );

            limparDadosAgendamento();

        } catch (error) {

            console.error(error);

            if (error.status === 409) {

                exibirMensagemCliente(
                    "O horário escolhido não está disponível. "
                    + "Escolha outro horário."
                );

            } else if (error.status === 400) {

                exibirMensagemCliente(
                    "Não foi possível realizar o agendamento. "
                    + "Confira os dados informados."
                );

            } else {

                exibirMensagemCliente(
                    "Não foi possível realizar o agendamento."
                );
            }

        } finally {

            confirmarAgendamentoButton.disabled =
                false;
        }
    }
);

carregarServicos();
