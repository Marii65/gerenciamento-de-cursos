package com.gerenciamentocursos.GerenciamentoDeCursos.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record AlunoRequestDTO(

    @Schema(description = "Nome completo do aluno", example = "Maria Luiza Santos Nascimento")
    @NotBlank(message = "O nome precisa ser preenchido.")
    @Size(max = 100, message = "O nome deve conter no máximo 100 caracteres.")
    String nome,

    @Schema(description = "E-mail válido do aluno", example = "maria@email.com")
    @NotBlank(message = "O email precisa ser preenchido.")
    @Size(max = 100, message = "O email deve conter no máximo 100 caracteres.")
    @Email(message = "O email deve possuir um formato válido.")
    String email,

    @Schema(description = "Data de nascimento do aluno", example = "2007-07-04")
    @NotNull(message = "A data de nascimento precisa ser preenchido.")
    @PastOrPresent(message = "A data de nascimento não pode estar no futuro.")
    LocalDate dataNascimento
){
}

