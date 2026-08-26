package br.com.dsin.salaoleila.dto.request;

import br.com.dsin.salaoleila.model.StatusAgendamento;
import jakarta.validation.constraints.NotNull;

public record AtualizarStatusAgendamentoRequest(

        @NotNull(message = "Status é obrigatório")
        StatusAgendamento status
) {
}
