package br.com.dsin.salaoleila.controller;

import br.com.dsin.salaoleila.model.Agendamento;
import br.com.dsin.salaoleila.service.AgendamentoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    private final AgendamentoService agendamentoService;

    public AgendamentoController(AgendamentoService agendamentoService) {
        this.agendamentoService = agendamentoService;
    }

    @PostMapping
    public ResponseEntity<Agendamento> criar(
            @RequestBody Agendamento agendamento) {

        Agendamento novoAgendamento =
                agendamentoService.criar(agendamento);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(novoAgendamento);
    }

    @GetMapping
    public ResponseEntity<List<Agendamento>> listar() {
        return ResponseEntity.ok(agendamentoService.listar());
    }
}
