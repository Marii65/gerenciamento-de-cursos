package com.gerenciamentocursos.GerenciamentoDeCursos.dto.request;

import com.gerenciamentocursos.GerenciamentoDeCursos.entity.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record MatriculaStatusRequestDTO(
        @Schema(description = "Novo status da matrícula (CANCELADA ou CONCLUIDA)", example = "CONCLUIDA")
        @NotNull
        Status status
) {}
