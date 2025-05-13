package com.projetointegrador.estoque.exeption;

public class DatasInvalidasException extends RuntimeException {
    public DatasInvalidasException(String message) {
        super(message);
    }
}
