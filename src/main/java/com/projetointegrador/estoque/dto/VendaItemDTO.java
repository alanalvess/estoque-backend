package com.projetointegrador.estoque.dto;

import com.projetointegrador.estoque.model.Produto;
import com.projetointegrador.estoque.model.Venda;

import java.math.BigDecimal;

public record VendaItemDTO(
        Long id,
        Venda venda,
        Produto produto,
        Integer quantidade,
        BigDecimal valor,
        BigDecimal total

) {
}
