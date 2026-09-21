package com.gerenciamentocursos.GerenciamentoDeCursos.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Objeto de Transferência de Dados (DTO) para cadastro de novos usuários.
 * Contém as validações necessárias para os dados de registro.
 *
 * @param email E-mail único do novo usuário.
 * @param senha Senha do novo usuário (mínimo de 6 caracteres).
 */
public record UsuarioRequestDTO(

        @Schema(description = "E-mail válido para o novo usuário", example = "maria@email.com")
        @NotBlank(message = "O email precisa ser preenchido.")
        @Email(message = "O email deve possuir um formato válido.")
        String email,

        @Schema(description = "Senha de acesso (no mínimo 6 caracteres)", example = "123456")
        @NotBlank(message = "A senha precisa ser preenchida.")
        @Size(min = 6, message = "A senha deve possuir no mínimo 6 caracteres.")
        String senha
) {}
