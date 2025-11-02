package br.com.fiap.ondetamoto.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthDTO(
        @NotBlank String email,
        @NotBlank String senha
) {
}
