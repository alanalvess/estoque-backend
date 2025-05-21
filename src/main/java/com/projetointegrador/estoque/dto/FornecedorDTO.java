package com.projetointegrador.estoque.dto;

public record FornecedorDTO(
        Long id,
        String nome,
        String cnpj,
        String email,
        String telefone,
        String endereco
) {
}
