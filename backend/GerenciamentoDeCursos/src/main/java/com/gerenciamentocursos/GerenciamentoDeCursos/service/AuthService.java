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

/**
 * Serviço responsável por concentrar as regras de negócio de autenticação.
 * Realiza a validação e cadastro de novos usuários, assim como o processo de login
 * e emissão de tokens JWT.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    /**
     * Cadastra um novo usuário no sistema.
     * Verifica se o e-mail informado já está em uso, criptografa a senha antes de
     * persistir e salva o registro na base de dados.
     *
     * @param dto Objeto com as informações do novo usuário (e-mail e senha sem criptografia).
     * @throws IllegalArgumentException Se o e-mail fornecido já estiver cadastrado no banco.
     */
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

    /**
     * Autentica o usuário no sistema e gera um token JWT de acesso.
     * Utiliza o {@link AuthenticationManager} do Spring Security para conferir e-mail e senha.
     *
     * @param dto Objeto contendo as credenciais do usuário (e-mail e senha).
     * @return {@link LoginResponseDTO} contendo o token JWT gerado para o usuário.
     * @throws org.springframework.security.core.AuthenticationException Se as credenciais forem inválidas.
     */
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