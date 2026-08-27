package br.com.dsin.salaoleila.dto.request;

import br.com.dsin.salaoleila.model.StatusServicoAgendamento;
import jakarta.validation.constraints.NotNull;

public record AtualizarStatusServicoRequest(

        @NotNull(message = "Status é obrigatório")
        StatusServicoAgendamento status
) {
}
