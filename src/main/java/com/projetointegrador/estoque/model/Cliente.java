package com.projetointegrador.estoque.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.projetointegrador.estoque.dto.ClienteDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O Nome do cliente é obrigatório!")
    @Size(min = 3, max = 100, message = "O nome deve conter no mínimo 03 e no máximo 100 caracteres")
    private String nome;

    @NotBlank(message = "O CPF é obrigatório!")
    @Column(unique = true, length = 14)
    private String cpf;

    @Email(message = "E-mail inválido")
    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String telefone;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("cliente")
    private List<Venda> vendas;

    public Cliente(ClienteDTO dto) {
        this.id = dto.id();
        this.nome = dto.nome();
        this.cpf = dto.cpf();
        this.email = dto.email();
        this.telefone = dto.telefone();
    }
}
