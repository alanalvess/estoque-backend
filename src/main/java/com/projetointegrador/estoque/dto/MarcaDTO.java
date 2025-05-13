package com.projetointegrador.estoque.dto;

import jakarta.validation.constraints.NotBlank;

public record MarcaDTO(
        Long id,
        @NotBlank(message = "O nome da categoria é obrigatório") String nome) {
}
