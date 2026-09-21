package com.gerenciamentocursos.GerenciamentoDeCursos.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CursoRequestDTO (
    @Schema(description = "Nome do curso", example = "Desenvolvimento Java Spring Boot")
    @NotBlank(message = "O nome precisa ser preenchido")
    @Size(max = 100, message = "O nome deve conter no máximo 100 caracteres.")
    String nome,

    @Schema(description = "Descrição detalhada do curso", example = "Curso prático cobrindo REST API, JPA, MySQL e Swagger.")
    @Size(max = 500, message = "A descrição deve conter no máximo 500 caracteres.")
    String descricao,

    @Schema(description = "Carga horária total em horas", example = "40")
    @NotNull(message = "A carga horária precisa ser preenchida.")
    @Positive(message = "A carga horária deve ser maior que zero.")
    Integer cargaHoraria
    ){
}
