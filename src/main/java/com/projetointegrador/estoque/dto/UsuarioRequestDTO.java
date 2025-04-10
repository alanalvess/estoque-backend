package com.projetointegrador.estoque.dto;

import com.projetointegrador.estoque.enums.Role;
import jakarta.validation.constraints.Email;

import java.util.Set;

public record UsuarioRequestDTO(
        Long id,
        String nome,
        @Email(message = "O email deve ser válido") String email,
        Set<Role> roles
) {
}
