package br.com.dsin.salaoleila.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record AgendamentoRequest(

        @NotNull(message = "Data é obrigatória")
        @FutureOrPresent(message = "Data do agendamento não pode estar no passado")
        LocalDate data,

        @NotNull(message = "Horário é obrigatório")
        LocalTime horario,

        @NotNull(message = "Cliente é obrigatório")
        @Positive(message = "Cliente inválido")
        Long clienteId,

        @NotEmpty(message = "Informe pelo menos um serviço")
        List<
                @NotNull(message = "Serviço inválido")
                @Positive(message = "Serviço inválido")
                        Long
                > servicoIds
) {
}
