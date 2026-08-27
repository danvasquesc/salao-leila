package br.com.dsin.salaoleila.dto.response;

import br.com.dsin.salaoleila.model.StatusAgendamento;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record AgendamentoResponse(
        Long id,
        LocalDate data,
        LocalTime horario,
        StatusAgendamento status,
        ClienteResponse cliente,
        List<ServicoResponse> servicos
) {
}
