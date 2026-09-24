package com.gerenciamentocursos.GerenciamentoDeCursos.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;


public record MatriculaRequestDTO (

        @Schema(description = "ID único do aluno a ser matriculado", example = "d3ee2929-212b-4077-af84-694a0e69b8e1")
        @NotNull(message = "O aluno precisa ser informado.")
        UUID alunoId,

        @Schema(description = "ID único do curso desejado", example = "a1b2c3d4-e5f6-7890-abcd-ef1234567890")
        @NotNull(message = "O curso precisa ser informado.")
        UUID cursoId,

        @Schema(description = "data prevista para conclusão do curso", example = "2026-12-31")
        @NotNull
        @Future
        LocalDate dataPrevisaoConclusao
){
}
