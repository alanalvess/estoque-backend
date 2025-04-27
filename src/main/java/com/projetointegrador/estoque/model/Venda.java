package com.projetointegrador.estoque.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.projetointegrador.estoque.dto.VendaDTO;
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
@Table(name = "tb_vendas")
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "A data da venda é obrigatória")
    private LocalDateTime dataVenda = LocalDateTime.now();

    private BigDecimal total = BigDecimal.ZERO;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    @JsonIgnoreProperties("vendas")
    private Cliente cliente;

    @OneToMany(mappedBy = "venda", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("venda")
    private List<VendaItem> itens = new ArrayList<>();

    public Venda(VendaDTO dto) {
        this.id = dto.id();
        this.dataVenda = dto.dataVenda();
        this.total = dto.total();
        this.cliente = dto.cliente();
    }
}
