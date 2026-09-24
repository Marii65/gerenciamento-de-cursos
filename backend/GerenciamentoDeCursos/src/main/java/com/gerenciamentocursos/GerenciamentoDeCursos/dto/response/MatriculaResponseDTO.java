package com.gerenciamentocursos.GerenciamentoDeCursos.dto.response;

import com.gerenciamentocursos.GerenciamentoDeCursos.entity.Aluno;
import com.gerenciamentocursos.GerenciamentoDeCursos.entity.Curso;
import com.gerenciamentocursos.GerenciamentoDeCursos.entity.Status;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.UUID;

public record MatriculaResponseDTO (

        @Schema(description = "Identificador único da matrícula", example = "e8f7c6b5-a4d3-2109-8765-43210fedcba9")
        UUID id,

        @Schema(description = "Identificador do aluno vinculado", example = "d3ee2929-212b-4077-af84-694a0e69b8e1")
        UUID alunoId,

        @Schema(description = "Nome do aluno vinculado", example = "Maria Luiza Santos Nascimento")
        String alunoNome,

        @Schema(description = "Identificador do curso vinculado", example = "a1b2c3d4-e5f6-7890-abcd-ef1234567890")
        UUID cursoId,

        @Schema(description = "Nome do curso vinculado", example = "Desenvolvimento Java Spring Boot")
        String cursoNome,

        @Schema(description = "Data em que a matrícula foi realizada", example = "2026-09-21")
        LocalDate dataMatricula,

        @Schema(description = "Data em que o curso será concluído", example = "2026-09-21")
        LocalDate dataPrevisaoConclusao,

        @Schema(description = "Status atual da matrícula (ATIVA, CANCELADA, CONCLUIDA)", example = "ATIVA")
        Status status

){
}
