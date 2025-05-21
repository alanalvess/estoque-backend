package com.projetointegrador.estoque.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CategoriaDuplicadaException extends RuntimeException {
    public CategoriaDuplicadaException(String message) {
        super("Já existe uma categoria com o nome: " + message);
    }
}