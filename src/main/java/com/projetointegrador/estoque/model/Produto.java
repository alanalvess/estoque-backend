package com.projetointegrador.estoque.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.projetointegrador.estoque.dto.ProdutoDTO;
import com.projetointegrador.estoque.enums.UnidadeMedida;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do produto é obrigatório")
    @Column(unique = true)
    private String nome;

    @NotNull(message = "O preço do produto é obrigatório")
    @Min(value = 0, message = "O preço deve ser positivo")
    private BigDecimal valor;

    @Min(value = 0, message = "A quantidade em estoque deve ser positiva")
    private Integer quantidade;

    @Enumerated(EnumType.STRING)
    private UnidadeMedida unidadeMedida;

    private String codigo;
    private String marca;
    private Integer estoqueMinimo;
    private Integer estoqueMaximo;
    private String validade;
    private String dataEntrada;
    private String dataSaida;
    private String descricao;

    private boolean disponivel;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    @JsonIgnoreProperties("produtos")
    @NotNull(message = "É obrigatório preencher a categoria do produto")
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "fornecedor_id")
    @JsonIgnoreProperties("produtos")
    @NotNull(message = "É obrigatório preencher o fornecedor do produto")
    private Fornecedor fornecedor;

    public Produto(ProdutoDTO dto) {
        this.id = dto.id();
        this.nome = dto.nome();
        this.descricao = dto.descricao();
        this.valor = dto.valor();
        this.quantidade = dto.quantidade();
        this.codigo = dto.codigo();
        this.unidadeMedida = dto.unidadeMedida();
        this.marca = dto.marca();
        this.estoqueMinimo = dto.estoqueMinimo();
        this.estoqueMaximo = dto.estoqueMaximo();
        this.validade = dto.validade();
        this.dataEntrada = dto.dataEntrada();
        this.dataSaida = dto.dataSaida();
        this.categoria = dto.categoria();
        this.fornecedor = dto.fornecedor();
        if (dto.quantidade() == null || dto.quantidade() <= 0) {
            this.disponivel = false;
        } else {
            this.disponivel = dto.disponivel();
        }
    }
}