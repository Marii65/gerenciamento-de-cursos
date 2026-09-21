package com.gerenciamentocursos.GerenciamentoDeCursos.exception;

public class CursoNotFoundException extends RuntimeException{
    public CursoNotFoundException(String mensagem){
        super(mensagem);
    }
}
