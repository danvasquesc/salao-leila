package br.com.dsin.salaoleila.controller;

import br.com.dsin.salaoleila.dto.request.AgendamentoRequest;
import br.com.dsin.salaoleila.dto.response.AgendamentoResponse;
import br.com.dsin.salaoleila.service.AgendamentoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    private final AgendamentoService agendamentoService;

    public AgendamentoController(
            AgendamentoService agendamentoService) {

        this.agendamentoService = agendamentoService;
    }

    @PostMapping
    public ResponseEntity<AgendamentoResponse> criar(
            @Valid @RequestBody AgendamentoRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(agendamentoService.criar(request));
    }

    @GetMapping
    public ResponseEntity<List<AgendamentoResponse>> listar() {

        return ResponseEntity.ok(
                agendamentoService.listar()
        );
    }
}
