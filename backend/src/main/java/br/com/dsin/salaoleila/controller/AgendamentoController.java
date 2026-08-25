package br.com.dsin.salaoleila.controller;

import br.com.dsin.salaoleila.dto.request.AgendamentoRequest;
import br.com.dsin.salaoleila.dto.request.AgendamentoUpdateRequest;
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

    @GetMapping("/{id}")
    public ResponseEntity<AgendamentoResponse> buscarPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                agendamentoService.buscarPorId(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<AgendamentoResponse> atualizar(
            @PathVariable Long id,
            @Valid
            @RequestBody AgendamentoUpdateRequest request) {

        return ResponseEntity.ok(
                agendamentoService.atualizar(id, request)
        );
    }
}
