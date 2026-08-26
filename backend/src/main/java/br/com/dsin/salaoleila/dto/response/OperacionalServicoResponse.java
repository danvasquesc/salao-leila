package br.com.dsin.salaoleila.dto.response;

import br.com.dsin.salaoleila.model.StatusServicoAgendamento;

public record OperacionalServicoResponse(
        Long itemId,
        Long servicoId,
        String nome,
        Integer duracao,
        StatusServicoAgendamento status
) {
}
