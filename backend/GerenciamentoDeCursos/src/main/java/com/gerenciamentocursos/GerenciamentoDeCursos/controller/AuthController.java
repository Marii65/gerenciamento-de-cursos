package com.gerenciamentocursos.GerenciamentoDeCursos.controller;

import com.gerenciamentocursos.GerenciamentoDeCursos.dto.request.LoginRequestDTO;
import com.gerenciamentocursos.GerenciamentoDeCursos.dto.request.UsuarioRequestDTO;
import com.gerenciamentocursos.GerenciamentoDeCursos.dto.response.LoginResponseDTO;
import com.gerenciamentocursos.GerenciamentoDeCursos.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para gerenciar operações de autenticação e cadastro de usuários.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Endpoints para login e cadastro de usuários no sistema")
public class AuthController {

    private final AuthService authService;

    /**
     * Cadastra um novo usuário no sistema.
     *
     * @param dto Objeto contendo os dados do novo usuário (e-mail e senha).
     * @return {@link ResponseEntity} com status 200 (OK) após o cadastro bem-sucedido.
     */
    @Operation(summary = "Cadastrar novo usuário", description = "Cria uma nova conta de usuário no sistema com e-mail e senha.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de requisição inválidos"),
            @ApiResponse(responseCode = "409", description = "E-mail já cadastrado no sistema")
    })
    @PostMapping("/cadastro")
    public ResponseEntity<Void> cadastrar(
            @Valid @RequestBody UsuarioRequestDTO dto) {

        authService.cadastrar(dto);

        return ResponseEntity.ok().build();
    }

    /**
     * Autentica um usuário existente e gera um token JWT de acesso.
     *
     * @param dto Objeto contendo as credenciais do usuário (e-mail e senha).
     * @return {@link ResponseEntity} contendo o token JWT gerado em {@link LoginResponseDTO}.
     */
    @Operation(summary = "Realizar Login", description = "Autentica as credenciais do usuário e retorna um token JWT para acesso às rotas protegidas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas (e-mail ou senha incorretos)")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO dto) {

        return ResponseEntity.ok(
                authService.login(dto)
        );
    }
}