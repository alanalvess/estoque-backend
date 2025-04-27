package com.projetointegrador.estoque.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.projetointegrador.estoque.dto.FornecedorDTO;
import com.projetointegrador.estoque.dto.PedidoCompraDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_pedidos_compra")
public class PedidoCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "A data do pedido é obrigatória")
    private LocalDateTime dataPedido = LocalDateTime.now();

    private BigDecimal total = BigDecimal.ZERO;

    @ManyToOne
    @JoinColumn(name = "fornecedor_id", nullable = false)
    @JsonIgnoreProperties("pedidos")
    private Fornecedor fornecedor;

    @OneToMany(mappedBy = "pedidoCompra", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("pedidoCompra")
    private List<PedidoItem> itens = new ArrayList<>();

    private String status;

    public PedidoCompra(PedidoCompraDTO dto) {
        this.id = dto.id();
        this.dataPedido = dto.dataPedido();
        this.fornecedor = dto.fornecedor();
        this.status = dto.status();
        this.total = dto.total();
    }
}
