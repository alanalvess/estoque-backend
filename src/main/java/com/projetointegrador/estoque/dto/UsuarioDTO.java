package com.projetointegrador.estoque.dto;

import com.projetointegrador.estoque.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public record UsuarioDTO(
        Long id,
        @NotBlank(message = "O nome do usuário é obrigatório") String nome,
        @NotBlank(message = "O email do usuário é obrigatório") @Email(message = "O email deve ser válido") String email,
        String token,
        Set<Role> roles
) {
}
