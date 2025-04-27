package com.projetointegrador.estoque.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.projetointegrador.estoque.dto.VendaItemDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_venda_itens")
public class VendaItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "venda_id", nullable = false)
    @JsonIgnoreProperties("itens")
    private Venda venda;

    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    @JsonIgnoreProperties("vendaItens")
    private Produto produto;

    @Min(value = 1, message = "A quantidade mínima é 1")
    private Integer quantidade;

    @NotNull(message = "O preço unitário é obrigatório")
    private BigDecimal valor;

    private BigDecimal total = BigDecimal.ZERO;

    public VendaItem(VendaItemDTO dto) {
        this.id = dto.id();
        this.produto = dto.produto();
        this.quantidade = dto.quantidade();
        this.valor = dto.valor();
        this.venda = dto.venda();
        this.total = dto.total();
    }
}
