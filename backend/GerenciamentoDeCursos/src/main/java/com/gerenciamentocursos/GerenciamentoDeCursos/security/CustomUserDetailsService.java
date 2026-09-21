package com.gerenciamentocursos.GerenciamentoDeCursos.security;

import com.gerenciamentocursos.GerenciamentoDeCursos.entity.Usuario;
import com.gerenciamentocursos.GerenciamentoDeCursos.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Serviço que implementa a interface {@link UserDetailsService} do Spring Security.
 * Responsável por buscar o usuário na base de dados pelo e-mail e convertê-lo
 * em um objeto {@link UserDetails} legível pelo ecossistema do Spring Security.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    /**
     * Busca o usuário no banco de dados através do e-mail e constrói o objeto de detalhes de usuário.
     *
     * @param email E-mail do usuário buscado (usado como 'username' no sistema).
     * @return Objeto {@link UserDetails} contendo login, senha e perfis (roles) do usuário.
     * @throws UsernameNotFoundException Lançada quando o e-mail não é encontrado no banco de dados.
     */
    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "Usuário não encontrado."
                        ));

        return User.builder()
                .username(usuario.getEmail())
                .password(usuario.getSenha())
                .roles("USER")
                .build();
    }
}