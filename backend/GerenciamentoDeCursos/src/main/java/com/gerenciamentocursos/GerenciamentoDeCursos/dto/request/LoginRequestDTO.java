package com.gerenciamentocursos.GerenciamentoDeCursos.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(

        @Schema(description = "E-mail válido do usuário", example = "maria@email.com")
        @NotBlank
        @Email
        String email,

        @Schema(description = "Senha do usuário", example = "1234")
        @NotBlank
        String senha
) {}
