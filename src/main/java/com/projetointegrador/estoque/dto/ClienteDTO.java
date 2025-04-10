package com.projetointegrador.estoque.dto;

import jakarta.validation.constraints.NotBlank;

public record ClienteDTO(
        Long id,
        @NotBlank(message = "O nome do cliente é obrigatório") String nome,
        @NotBlank(message = "O cpf do cliente é obrigatório") String cpf,
        @NotBlank(message = "O email do cliente é obrigatório") String email,
        @NotBlank(message = "O telefone do cliente é obrigatório") String telefone
) {
}

