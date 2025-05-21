package com.projetointegrador.estoque.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.projetointegrador.estoque.dto.FornecedorDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_fornecedores")
public class Fornecedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do fornecedor é obrigatório")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
    private String nome;

    @Column(unique = true, length = 14)
    private String cnpj;

    @Email(message = "E-mail inválido")
    private String email;

    private String telefone;

    private String endereco;

    @OneToMany(mappedBy = "fornecedor", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Produto> produtos = new ArrayList<>();

    public Fornecedor(FornecedorDTO dto) {
        this.id = dto.id();
        this.nome = dto.nome();
        this.cnpj = dto.cnpj();
        this.email = dto.email();
        this.telefone = dto.telefone();
        this.endereco = dto.endereco();
    }
}
