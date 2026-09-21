package com.gerenciamentocursos.GerenciamentoDeCursos.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Serviço responsável por gerar, assinar e extrair informações dos tokens JWT.
 */
@Service
public class JwtService {

    private final SecretKey secretKey;

    /**
     * Construtor que inicializa a chave secreta a partir das configurações do aplicativo.
     *
     * @param secret Chave secreta configurada no arquivo {@code application.properties}.
     */
    public JwtService(
            @Value("${jwt.secret}") String secret) {

        this.secretKey = Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );
    }

    /**
     * Gera um novo token JWT assinado para o e-mail fornecido.
     * O token possui validade de 24 horas (86.400.000 milissegundos).
     *
     * @param email E-mail do usuário autenticado.
     * @return String contendo o token JWT gerado.
     */
    public String gerarToken(String email) {

        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(
                        new Date(System.currentTimeMillis() + 86400000)
                )
                .signWith(secretKey)
                .compact();
    }

    /**
     * Extrai e valida o e-mail (subject) contido em um token JWT.
     *
     * @param token Token JWT recebido no cabeçalho da requisição.
     * @return E-mail do usuário extraído do payload do token.
     */
    public String extrairEmail(String token) {

        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}
