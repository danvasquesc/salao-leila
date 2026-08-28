const mensagemLogin =
    document.getElementById(
        "mensagemLogin"
    );

const esqueciSenhaButton =
    document.getElementById(
        "esqueciSenha"
    );

const recuperacaoSenha =
    document.getElementById(
        "recuperacaoSenha"
    );

const formRecuperacao =
    document.getElementById(
        "formRecuperacao"
    );

const telefoneRecuperacao =
    document.getElementById(
        "telefoneRecuperacao"
    );

const mensagemRecuperacao =
    document.getElementById(
        "mensagemRecuperacao"
    );


function verificarErroLogin() {

    const parametros =
        new URLSearchParams(
            window.location.search
        );

    if (parametros.has("erro")) {

        mensagemLogin.innerHTML = `
            <div class="message message-error">
                Usuário ou senha inválidos.
            </div>
        `;
    }
}


esqueciSenhaButton.addEventListener(
    "click",
    () => {

        recuperacaoSenha.hidden =
            !recuperacaoSenha.hidden;

        mensagemRecuperacao.textContent = "";
    }
);


telefoneRecuperacao.addEventListener(
    "input",
    event => {

        event.target.value =
            event.target.value
                .replace(/\D/g, "")
                .slice(0, 11);
    }
);


formRecuperacao.addEventListener(
    "submit",
    event => {

        event.preventDefault();

        const telefone =
            telefoneRecuperacao.value;

        if (
            telefone.length !== 10
            && telefone.length !== 11
        ) {

            mensagemRecuperacao.textContent =
                "Informe um telefone válido "
                + "com 10 ou 11 números.";

            return;
        }

        mensagemRecuperacao.textContent =
            "Solicitação registrada. "
            + "Em uma versão de produção, "
            + "as instruções seriam enviadas "
            + "por SMS.";

        formRecuperacao.reset();
    }
);


verificarErroLogin();
