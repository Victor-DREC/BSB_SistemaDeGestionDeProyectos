package com.krakedev.proyectos.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    // Tiempo de expiración: 1 hora
    private static final long EXPIRACION = 3600000;

    // Generar token
    public String generarToken(String username, String rol) {

        Algorithm algoritmo = Algorithm.HMAC256(secret);

        return JWT.create()
                .withSubject(username)
                .withClaim("rol", rol)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRACION))
                .sign(algoritmo);
    }

    // Validar token
    public DecodedJWT validarToken(String token) {

        Algorithm algoritmo = Algorithm.HMAC256(secret);

        JWTVerifier verificador = JWT.require(algoritmo).build();

        return verificador.verify(token);
    }
}