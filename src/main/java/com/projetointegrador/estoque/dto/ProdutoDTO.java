package com.projetointegrador.estoque.dto;

import com.projetointegrador.estoque.enums.UnidadeMedida;
import com.projetointegrador.estoque.model.Categoria;
import com.projetointegrador.estoque.model.Fornecedor;
import com.projetointegrador.estoque.model.Marca;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ProdutoDTO(
        Long id,
        String nome,
        String descricao,
        BigDecimal valor,
        Integer quantidade,
        Boolean disponivel,
        UnidadeMedida unidadeMedida,
        String codigo,
        Integer estoqueMinimo,
        Integer estoqueMaximo,
        LocalDate dataValidade,
        LocalDate dataEntrada,
        LocalDate dataSaida,
        Categoria categoria,
        Marca marca,
        Fornecedor fornecedor
) {
}
