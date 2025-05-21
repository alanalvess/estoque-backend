package com.projetointegrador.estoque.dto;

import com.projetointegrador.estoque.model.Produto;

import java.math.BigDecimal;

public record PedidoItemDTO(
        Long id,
        Produto produto,
        Integer quantidade,
        BigDecimal valor
) {
}
