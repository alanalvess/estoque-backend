package com.projetointegrador.estoque.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.projetointegrador.estoque.dto.CategoriaDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_categorias")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome da categoria é obrigatório")
    @Column(unique = true)
    private String nome;

    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Produto> produtos = new ArrayList<>();

    public Categoria(CategoriaDTO dto) {
        this.id = dto.id();
        this.nome = dto.nome();
    }
}
