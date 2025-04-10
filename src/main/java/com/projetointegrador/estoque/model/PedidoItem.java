package com.projetointegrador.estoque.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.projetointegrador.estoque.dto.PedidoItemDTO;
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
@Table(name = "tb_pedidos_item")
public class PedidoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pedido_compra_id", nullable = false)
    @JsonIgnoreProperties("itens")
    private PedidoCompra pedidoCompra;

    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    @JsonIgnoreProperties("pedidoItens")
    private Produto produto;

    @Min(value = 1, message = "A quantidade mínima é 1")
    private int quantidade;

    @NotNull(message = "O preço unitário é obrigatório")
    private BigDecimal valor;

    public BigDecimal calcularSubtotal() {
        return valor.multiply(BigDecimal.valueOf(quantidade));
    }

    public PedidoItem(PedidoItemDTO dto) {
        this.id = dto.id();
        this.produto = dto.produto();
        this.quantidade = dto.quantidade();
        this.valor = dto.valor();
    }

    public void atualizar(PedidoItemDTO dto) {
        if (dto.produto() != null) {
            this.produto = dto.produto();
        }
        if (dto.quantidade() != 0) {
            this.quantidade = dto.quantidade();
        }
        if (dto.valor() != null && dto.valor().compareTo(BigDecimal.ZERO) != 0) {
            this.valor = dto.valor();
        }
    }
}