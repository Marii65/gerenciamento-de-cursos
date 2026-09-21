package com.gerenciamentocursos.GerenciamentoDeCursos.exception;

public class AlunoNotFoundException  extends RuntimeException{
    public AlunoNotFoundException(String mensagem){
        super(mensagem);
    }
}
