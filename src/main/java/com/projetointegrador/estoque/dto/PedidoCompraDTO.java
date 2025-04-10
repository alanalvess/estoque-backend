package com.projetointegrador.estoque.dto;

import com.projetointegrador.estoque.model.Fornecedor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PedidoCompraDTO(
        Long id,
        LocalDateTime dataPedido,
        Fornecedor fornecedor,
        String status,
        BigDecimal total
) {
}
