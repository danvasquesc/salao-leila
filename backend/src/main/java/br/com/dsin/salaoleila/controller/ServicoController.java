package br.com.dsin.salaoleila.controller;

import br.com.dsin.salaoleila.dto.request.ServicoRequest;
import br.com.dsin.salaoleila.dto.response.ServicoResponse;
import br.com.dsin.salaoleila.service.ServicoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/servicos")
public class ServicoController {

    private final ServicoService servicoService;

    public ServicoController(ServicoService servicoService) {
        this.servicoService = servicoService;
    }

    @PostMapping
    public ResponseEntity<ServicoResponse> criar(
            @Valid @RequestBody ServicoRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(servicoService.criar(request));
    }

    @GetMapping
    public ResponseEntity<List<ServicoResponse>> listar() {

        return ResponseEntity.ok(servicoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicoResponse> buscarPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(servicoService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServicoResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ServicoRequest request) {

        return ResponseEntity.ok(
                servicoService.atualizar(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(
            @PathVariable Long id) {

        servicoService.excluir(id);

        return ResponseEntity.noContent().build();
    }
}
