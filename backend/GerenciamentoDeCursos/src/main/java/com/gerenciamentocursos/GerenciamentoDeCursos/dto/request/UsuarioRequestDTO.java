package com.gerenciamentocursos.GerenciamentoDeCursos.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioRequestDTO(

        @Schema(description = "E-mail válido do usuário", example = "maria@email.com")
        @NotBlank(message = "O email precisa ser preenchido.")
        @Email(message = "O email deve possuir um formato válido.")
        String email,

        @Schema(description = "Senha do usuário", example = "1234")
        @NotBlank(message = "A senha precisa ser preenchida.")
        @Size(min = 6, message = "A senha deve possuir no mínimo 6 caracteres.")
        String senha
) {}
