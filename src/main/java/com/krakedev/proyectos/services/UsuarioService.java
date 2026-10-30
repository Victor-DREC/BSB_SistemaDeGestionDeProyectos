package com.krakedev.proyectos.services;

import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.krakedev.proyectos.entidades.Usuario;
import com.krakedev.proyectos.repositories.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Registrar un usuario con contraseña cifrada
    public Usuario registrar(Usuario usuario) {

        Optional<Usuario> usuarioExistente =
                usuarioRepository.findByUsername(usuario.getUsername());

        if (usuarioExistente.isPresent()) {
            throw new RuntimeException("El nombre de usuario ya existe");
        }

        String passwordCifrada =
                BCrypt.hashpw(usuario.getPassword(), BCrypt.gensalt(10));

        usuario.setPassword(passwordCifrada);

        return usuarioRepository.save(usuario);
    }

    // Autenticar un usuario
    public Optional<Usuario> autenticar(String username, String password) {

        Optional<Usuario> usuarioEncontrado =
                usuarioRepository.findByUsername(username);

        if (usuarioEncontrado.isPresent()) {

            Usuario usuario = usuarioEncontrado.get();

            if (BCrypt.checkpw(password, usuario.getPassword())) {
                return Optional.of(usuario);
            }
        }

        return Optional.empty();
    }
}