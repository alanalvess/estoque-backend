package com.projetointegrador.estoque.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UsuarioNaoAutorizadoException extends RuntimeException {
    public UsuarioNaoAutorizadoException(String message) {
        super("Email ou senha inválidos: " + message);
    }
}
