package com.gerenciamentocursos.GerenciamentoDeCursos.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro HTTP executado uma única vez por requisição.
 * Intercepta as requisições para verificar a presença de um token JWT no cabeçalho 'Authorization',
 * validando o usuário e autenticando-o no contexto do Spring Security.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    /**
     * Executa a lógica de interceptação, extração do token e autenticação no contexto de segurança.
     *
     * @param request Requisição HTTP recebida.
     * @param response Resposta HTTP a ser enviada.
     * @param filterChain Cadeia de filtros do Spring Security.
     * @throws ServletException Se ocorrer erro no processamento do servlet.
     * @throws IOException Se ocorrer erro de entrada/saída.
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authorizationHeader =
                request.getHeader("Authorization");

        if (authorizationHeader == null ||
                !authorizationHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;
        }

        String token = authorizationHeader.substring(7);

        try {

            String email = jwtService.extrairEmail(token);

            UserDetails userDetails =
                    userDetailsService.loadUserByUsername(email);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

            authentication.setDetails(
                    new WebAuthenticationDetailsSource()
                            .buildDetails(request)
            );

            SecurityContextHolder
                    .getContext()
                    .setAuthentication(authentication);

        } catch (RuntimeException exception) {
            // Token inválido ou expirado.
            // A requisição seguirá sem autenticação.
        }

        filterChain.doFilter(request, response);
    }
}
