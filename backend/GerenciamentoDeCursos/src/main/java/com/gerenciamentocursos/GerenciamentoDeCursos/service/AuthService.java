package com.gerenciamentocursos.GerenciamentoDeCursos.service;

import com.gerenciamentocursos.GerenciamentoDeCursos.dto.request.LoginRequestDTO;
import com.gerenciamentocursos.GerenciamentoDeCursos.dto.request.UsuarioRequestDTO;
import com.gerenciamentocursos.GerenciamentoDeCursos.dto.response.LoginResponseDTO;
import com.gerenciamentocursos.GerenciamentoDeCursos.entity.Usuario;
import com.gerenciamentocursos.GerenciamentoDeCursos.repository.UsuarioRepository;
import com.gerenciamentocursos.GerenciamentoDeCursos.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public void cadastrar(UsuarioRequestDTO dto) {

        if (usuarioRepository.existsByEmail(dto.email())) {
            throw new IllegalArgumentException(
                    "Email já cadastrado."
            );
        }

        Usuario usuario = new Usuario();

        usuario.setEmail(dto.email());

        usuario.setSenha(
                passwordEncoder.encode(dto.senha())
        );

        usuarioRepository.save(usuario);
    }

    public LoginResponseDTO login(LoginRequestDTO dto) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.email(),
                        dto.senha()
                )
        );

        String token = jwtService.gerarToken(dto.email());

        return new LoginResponseDTO(token);
    }
}