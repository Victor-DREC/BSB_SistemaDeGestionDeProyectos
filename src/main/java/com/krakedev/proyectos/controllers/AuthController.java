package com.krakedev.proyectos.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.krakedev.proyectos.entidades.Usuario;
import com.krakedev.proyectos.services.UsuarioService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    // Registrar usuario
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

    // Iniciar sesión
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario usuario) {

        Optional<Usuario> usuarioAutenticado =
                usuarioService.autenticar(
                        usuario.getUsername(),
                        usuario.getPassword()
                );

        if (usuarioAutenticado.isPresent()) {

            Usuario encontrado = usuarioAutenticado.get();

            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("mensaje", "Autenticación correcta");
            respuesta.put("username", encontrado.getUsername());
            respuesta.put("rol", encontrado.getRol());

            return ResponseEntity.ok(respuesta);
        }

        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("error", "Usuario o contraseña incorrectos");

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(respuesta);
    }
}