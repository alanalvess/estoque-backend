package com.projetointegrador.estoque.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class FornecedorNaoEncontradoException extends RuntimeException {
    public FornecedorNaoEncontradoException(Long id) {
        super("Fornecedor com ID " + id + " não localizado.");
    }
}

