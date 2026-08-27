package br.com.dsin.salaoleila.dto.response;

public record ClienteResponse(
        Long id,
        String nome,
        String telefone,
        String email
) {
}
