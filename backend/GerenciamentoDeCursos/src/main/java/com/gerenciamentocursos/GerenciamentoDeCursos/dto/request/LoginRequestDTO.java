package com.gerenciamentocursos.GerenciamentoDeCursos.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Objeto de Transferência de Dados (DTO) para requisição de login.
 * Carrega as credenciais fornecidas pelo usuário para autenticação.
 *
 * @param email E-mail cadastrado do usuário.
 * @param senha Senha de acesso do usuário.
 */
public record LoginRequestDTO(

        @Schema(description = "E-mail cadastrado do usuário", example = "maria@email.com")
        @NotBlank
        @Email
        String email,

        @Schema(description = "Senha de acesso do usuário", example = "123456")
        @NotBlank
        String senha
) {}
