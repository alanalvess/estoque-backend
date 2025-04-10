package com.projetointegrador.estoque.dto;

import com.projetointegrador.estoque.enums.UnidadeMedida;
import com.projetointegrador.estoque.model.Categoria;
import com.projetointegrador.estoque.model.Fornecedor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProdutoDTO(
        Long id,
        String nome,
        String descricao,
        BigDecimal valor,
        Integer quantidade,
        Boolean disponivel,
        UnidadeMedida unidadeMedida,
        String codigo,
        String marca,
        Integer estoqueMinimo,
        Integer estoqueMaximo,
        String validade,
        String dataEntrada,
        String dataSaida,
        Categoria categoria,
        Fornecedor fornecedor
) {
}
