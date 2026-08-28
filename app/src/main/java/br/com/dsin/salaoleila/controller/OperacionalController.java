package br.com.dsin.salaoleila.controller;

import br.com.dsin.salaoleila.dto.request.AgendamentoUpdateRequest;
import br.com.dsin.salaoleila.dto.request.AtualizarStatusAgendamentoRequest;
import br.com.dsin.salaoleila.dto.request.AtualizarStatusServicoRequest;
import br.com.dsin.salaoleila.dto.response.AgendamentoResponse;
import br.com.dsin.salaoleila.dto.response.OperacionalAgendamentoResponse;
import br.com.dsin.salaoleila.service.OperacionalService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/operacional")
public class OperacionalController {

    private final OperacionalService operacionalService;

    public OperacionalController(
            OperacionalService operacionalService) {

        this.operacionalService = operacionalService;
    }

    @GetMapping("/agendamentos")
    public ResponseEntity<List<OperacionalAgendamentoResponse>>
    listarAgendamentos(

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate data) {

        return ResponseEntity.ok(
                operacionalService.listarPorData(data)
        );
    }

    @PutMapping("/agendamentos/{id}")
    public AgendamentoResponse alterarAgendamento(
            @PathVariable Long id,
            @Valid @RequestBody AgendamentoUpdateRequest request) {

        return operacionalService.alterarAgendamento(
                id,
                request
        );
    }

    @PatchMapping("/agendamentos/{id}/status")
    public ResponseEntity<OperacionalAgendamentoResponse>
    atualizarStatusAgendamento(

            @PathVariable Long id,

            @Valid
            @RequestBody
            AtualizarStatusAgendamentoRequest request) {

        return ResponseEntity.ok(
                operacionalService.atualizarStatusAgendamento(
                        id,
                        request.status()
                )
        );
    }

    @PatchMapping(
            "/agendamentos/{agendamentoId}"
                    + "/servicos/{itemId}/status"
    )
    public ResponseEntity<OperacionalAgendamentoResponse>
    atualizarStatusServico(

            @PathVariable Long agendamentoId,
            @PathVariable Long itemId,

            @Valid
            @RequestBody
            AtualizarStatusServicoRequest request) {

        return ResponseEntity.ok(
                operacionalService.atualizarStatusServico(
                        agendamentoId,
                        itemId,
                        request.status()
                )
        );
    }
}
