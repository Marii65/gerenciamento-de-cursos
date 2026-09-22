package com.gerenciamentocursos.GerenciamentoDeCursos.exception;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String mensagem) {
        super(mensagem);
    }
}
