package com.gerenciamentocursos.GerenciamentoDeCursos.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record AlunoResponseDTO(
    @Schema(description = "ID do aluno (criptografado)", example = "d3ee2929-212b-4077-af84-694a0e69b8e1")
    UUID id,
    @Schema(description = "Nome completo do aluno", example = "Maria Luiza Santos Nascimento")
    String nome,
    @Schema(description = "E-mail válido do aluno", example = "Maria@email.com")
    String email,
    @Schema(description = "Data de nascimento do aluno (Representada por ano, mês e dia)", example = "2007-07-04")
    LocalDate dataNascimento,
    @Schema(description = "Data dem que o aluno foi cadastrado (Gerado automaticamente)", example = "2026-09-21T14:59:4")
    LocalDateTime criadoEm
){
}
