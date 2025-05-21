package com.projetointegrador.estoque.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProdutoDuplicadoException extends RuntimeException {
    public ProdutoDuplicadoException(String nome) {
        super("Já existe um produto com o nome '" + nome + "'.");
    }
}
