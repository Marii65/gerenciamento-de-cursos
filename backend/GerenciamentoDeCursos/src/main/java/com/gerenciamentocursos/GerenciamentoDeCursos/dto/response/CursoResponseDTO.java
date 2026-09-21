package com.gerenciamentocursos.GerenciamentoDeCursos.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record CursoResponseDTO (

        @Schema(description = "Identificador único do curso", example = "a1b2c3d4-e5f6-7890-abcd-ef1234567890")
        UUID id,

        @Schema(description = "Nome do curso", example = "Desenvolvimento Java Spring Boot")
        String nome,

        @Schema(description = "Descrição detalhada do curso", example = "Curso prático cobrindo REST API, JPA, MySQL e Swagger.")
        String descricao,

        @Schema(description = "Carga horária em horas", example = "40")
        Integer cargaHoraria,

        @Schema(description = "Data e hora de criação do registro", example = "2026-09-21T14:59:04")
        LocalDateTime criadoEm
){
}
