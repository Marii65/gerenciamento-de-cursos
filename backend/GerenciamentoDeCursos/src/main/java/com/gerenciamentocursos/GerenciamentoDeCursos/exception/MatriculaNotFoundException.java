package com.gerenciamentocursos.GerenciamentoDeCursos.exception;

public class MatriculaNotFoundException extends RuntimeException{
    public MatriculaNotFoundException(String mensagem){
        super(mensagem);
    }
}
