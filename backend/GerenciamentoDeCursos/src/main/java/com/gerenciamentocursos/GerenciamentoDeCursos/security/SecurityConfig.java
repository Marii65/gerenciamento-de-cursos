package com.gerenciamentocursos.GerenciamentoDeCursos.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Classe de configuração central do Spring Security.
 * Define as regras de autenticação, permissões de rotas públicas/privadas,
 * gerenciamento de sessão stateless (sem estado) e inclusão de filtros personalizados.
 */
@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Construtor para injeção de dependências.
     *
     * @param jwtAuthenticationFilter Filtro personalizado para validação de tokens JWT.
     */
    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    /**
     * Define o algoritmo de criptografia de senhas utilizado pela aplicação.
     *
     * @return Instância de {@link BCryptPasswordEncoder}.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Provê o gerenciador de autenticação do Spring Security.
     *
     * @param configuration Configuração de autenticação do Spring.
     * @return O {@link AuthenticationManager} configurado.
     * @throws Exception Caso ocorra uma falha ao obter o gerenciador.
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration)
            throws Exception {

        return configuration.getAuthenticationManager();
    }

    /**
     * Configura a corrente de filtros de segurança da aplicação (SecurityFilterChain).
     *
     * @param http Instância de {@link HttpSecurity} para configurar as regras HTTP.
     * @return A corrente de filtros configurada.
     * @throws Exception Caso ocorra erro durante a configuração.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http)
            throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
                                "/api/auth/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**"
                        ).permitAll()

                        .anyRequest().authenticated()
                )

                .formLogin(form -> form.disable())

                .httpBasic(basic -> basic.disable())

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}