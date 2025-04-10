package com.projetointegrador.estoque.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FornecedorDuplicadoException extends RuntimeException {
    public FornecedorDuplicadoException(String campo, String valor) {
        super("Fornecedor com " + campo + " " + valor + " já existente.");
    }
}
