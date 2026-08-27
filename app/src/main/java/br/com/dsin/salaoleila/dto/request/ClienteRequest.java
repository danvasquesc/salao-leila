package br.com.dsin.salaoleila.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ClienteRequest(

        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
        String nome,

        @NotBlank(message = "Telefone é obrigatório")
        @Pattern(
                regexp = "\\d{10,11}",
                message = "Telefone deve conter 10 ou 11 números"
        )
        String telefone,

        @Email(message = "E-mail inválido")
        @Size(max = 100, message = "E-mail deve ter no máximo 100 caracteres")
        String email
) {
}