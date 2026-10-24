package com.example.authapi.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.example.authapi.details.UserDetailsImpl;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
public class JwtTokenService {

    private static final String SECRET_KEY = "4Z^XrroxR@dWxqf$mTTKwW$!@#qGr4P"; // Chave secreta utilizada para gerar e verificar o token

    private static final String ISSUER = "auth-api"; // Emissor do token

    public String generateToken(UserDetailsImpl user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY); // Algoritmo de assinatura do token

            return JWT.create()
                    .withIssuer(ISSUER)
                    .withIssuedAt(creationDate())
                    .withIssuedAt(expirationDate())
                    .withSubject(user.getUsername())
                    .sign(algorithm); // Gera o token assinado com a chave secreta
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error ao gerar token", exception);
        }
    }

    public String getSubjectFromToken(String token) {
        try {
            // Define o algoritmo HMAC SHA256 para verificar a assinatura do token passando a chave secreta definida
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            return JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build()
                    .verify(token)
                    .getSubject(); // Retorna o assunto (subject) do token, que é o email do usuário
        } catch (Exception exception) {
            throw new RuntimeException("Error ao verificar token", exception);
        }
    }

    private Instant creationDate() {
        return ZonedDateTime.now(ZoneId.of("America/Fortaleza")).toInstant();
    }

    private Instant expirationDate() {
        return ZonedDateTime.now(ZoneId.of("America/Fortaleza")).plusHours(4).toInstant();
    }
}
