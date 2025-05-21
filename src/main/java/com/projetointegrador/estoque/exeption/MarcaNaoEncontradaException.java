package com.projetointegrador.estoque.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MarcaNaoEncontradaException extends RuntimeException {
    public MarcaNaoEncontradaException(Long id) {
        super("Marca com ID " + id + " não encontrada.");
    }
}
