
package com.krakedev.proyectos.security;

import java.io.IOException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.krakedev.proyectos.services.TokenBlacklistService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null &&
                authorizationHeader.startsWith("Bearer ")) {

            String token = authorizationHeader.substring(7);

            try {
                // Comprobar si el token está en la lista negra
                if (tokenBlacklistService.estaInvalidado(token)) {
                    filterChain.doFilter(request, response);
                    return;
                }

                // Validar el token JWT
                DecodedJWT decodedJWT = jwtUtil.validarToken(token);

                String username = decodedJWT.getSubject();
                String rol = decodedJWT.getClaim("rol").asString();

                // Crear la autoridad del usuario
                SimpleGrantedAuthority autoridad =
                        new SimpleGrantedAuthority("ROLE_" + rol);

                // Crear la autenticación de Spring Security
                UsernamePasswordAuthenticationToken autenticacion =
                        new UsernamePasswordAuthenticationToken(
                                username,
                                null,
                                Collections.singletonList(autoridad)
                        );

                // Guardar la autenticación en el contexto
                SecurityContextHolder.getContext()
                        .setAuthentication(autenticacion);

            } catch (Exception e) {
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }
}
