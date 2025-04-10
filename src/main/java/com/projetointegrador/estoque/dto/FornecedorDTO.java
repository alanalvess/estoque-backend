package com.projetointegrador.estoque.dto;

import jakarta.validation.constraints.NotBlank;

public record FornecedorDTO(
        Long id,
        String nome,
        String cnpj,
        String email,
        String telefone,
        String endereco


) {
}
