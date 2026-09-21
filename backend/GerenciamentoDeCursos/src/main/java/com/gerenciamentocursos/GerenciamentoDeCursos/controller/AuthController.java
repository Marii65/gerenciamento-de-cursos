package com.gerenciamentocursos.GerenciamentoDeCursos.controller;

import com.gerenciamentocursos.GerenciamentoDeCursos.dto.request.LoginRequestDTO;
import com.gerenciamentocursos.GerenciamentoDeCursos.dto.request.UsuarioRequestDTO;
import com.gerenciamentocursos.GerenciamentoDeCursos.dto.response.LoginResponseDTO;
import com.gerenciamentocursos.GerenciamentoDeCursos.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/cadastro")
    public ResponseEntity<Void> cadastrar(
            @Valid @RequestBody UsuarioRequestDTO dto) {

        authService.cadastrar(dto);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO dto) {

        return ResponseEntity.ok(
                authService.login(dto)
        );
    }
}