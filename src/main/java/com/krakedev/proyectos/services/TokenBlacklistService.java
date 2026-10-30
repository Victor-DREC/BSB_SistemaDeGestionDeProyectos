
package com.krakedev.proyectos.services;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class TokenBlacklistService {

    private final Set<String> tokensInvalidos = ConcurrentHashMap.newKeySet();

    public void invalidarToken(String token) {
        tokensInvalidos.add(token);
    }

    public boolean estaInvalidado(String token) {
        return tokensInvalidos.contains(token);
    }
}
