package com.projetointegrador.estoque.dto;

import com.projetointegrador.estoque.model.Cliente;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record VendaDTO(
        Long id,
        LocalDateTime dataVenda,
        Cliente cliente,
        BigDecimal total

) {
}
