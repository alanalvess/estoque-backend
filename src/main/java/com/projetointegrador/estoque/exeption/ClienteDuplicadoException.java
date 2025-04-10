package com.projetointegrador.estoque.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ClienteDuplicadoException extends RuntimeException {
    public ClienteDuplicadoException(String cpf) {
        super("Cliente com cpf " + cpf + " não localizado.");
    }
}
