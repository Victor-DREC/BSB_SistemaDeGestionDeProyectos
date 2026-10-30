
package com.krakedev.proyectos.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.krakedev.proyectos.entidades.Usuario;
import com.krakedev.proyectos.security.JwtUtil;
import com.krakedev.proyectos.services.TokenBlacklistService;
import com.krakedev.proyectos.services.UsuarioService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@RequestBody Usuario usuario) {
        try {
            Usuario usuarioRegistrado = usuarioService.registrar(usuario);

            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("mensaje", "Usuario registrado correctamente");
            respuesta.put("username", usuarioRegistrado.getUsername());
            respuesta.put("rol", usuarioRegistrado.getRol());

            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);

        } catch (RuntimeException e) {
            Map<String, String> respuesta = new HashMap<>();
            respuesta.put("error", e.getMessage());

            return ResponseEntity.status(HttpStatus.CONFLICT).body(respuesta);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario usuario) {

        Optional<Usuario> usuarioAutenticado =
                usuarioService.autenticar(
                        usuario.getUsername(),
                        usuario.getPassword()
                );

        if (usuarioAutenticado.isPresent()) {

            Usuario encontrado = usuarioAutenticado.get();

            String token = jwtUtil.generarToken(
                    encontrado.getUsername(),
                    encontrado.getRol()
            );

            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("mensaje", "Autenticación correcta");
            respuesta.put("username", encontrado.getUsername());
            respuesta.put("rol", encontrado.getRol());
            respuesta.put("token", token);

            return ResponseEntity.ok(respuesta);
        }

        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("error", "Usuario o contraseña incorrectos");

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(respuesta);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            @RequestHeader("Authorization") String authorization) {

        Map<String, String> respuesta = new HashMap<>();

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            respuesta.put("error", "Token no proporcionado o formato incorrecto");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(respuesta);
        }

        String token = authorization.substring(7);

        try {
            jwtUtil.validarToken(token);
            tokenBlacklistService.invalidarToken(token);

            respuesta.put("mensaje", "Sesión cerrada correctamente");
            return ResponseEntity.ok(respuesta);

        } catch (Exception e) {
            respuesta.put("error", "Token inválido o expirado");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(respuesta);
        }
    }
    
    @GetMapping("/perfil")
    public ResponseEntity<?> perfil() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        Map<String, Object> respuesta = new HashMap<>();

        respuesta.put("username", authentication.getName());
        respuesta.put("authorities", authentication.getAuthorities());

        return ResponseEntity.ok(respuesta);
    }
}
