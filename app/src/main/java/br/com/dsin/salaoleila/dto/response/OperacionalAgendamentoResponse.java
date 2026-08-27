package br.com.dsin.salaoleila.dto.response;

import br.com.dsin.salaoleila.model.StatusAgendamento;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

// DTO especifico para o painel
public record OperacionalAgendamentoResponse(
        Long id,
        LocalDate data,
        LocalTime horario,
        StatusAgendamento status,
        Long clienteId,
        String clienteNome,
        String clienteTelefone,
        List<OperacionalServicoResponse> servicos
) {
}
