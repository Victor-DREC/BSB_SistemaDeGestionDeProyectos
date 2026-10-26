
package com.krakedev.proyectos.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.krakedev.proyectos.entidades.Proyecto;
import com.krakedev.proyectos.services.ProyectoService;

@RestController
@RequestMapping("/api/proyectos")
public class ProyectoController {

    private final ProyectoService proyectoService;

    public ProyectoController(ProyectoService proyectoService) {
        this.proyectoService = proyectoService;
    }

    @GetMapping
    public ResponseEntity<?> listar() {
        try {
            List<Proyecto> proyectos = proyectoService.listar();
            return ResponseEntity.ok(proyectos);
        } catch (Exception e) {
            return errorInterno(e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable int id) {
        try {
            Proyecto proyecto = proyectoService.buscarPorId(id);
            return ResponseEntity.ok(proyecto);
        } catch (ResponseStatusException e) {
            return manejarError(e);
        } catch (Exception e) {
            return errorInterno(e);
        }
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Proyecto proyecto) {
        try {
            Proyecto creado = proyectoService.crear(proyecto);
            return ResponseEntity.status(HttpStatus.CREATED).body(creado);
        } catch (Exception e) {
            return errorInterno(e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @PathVariable Integer id,
            @RequestBody Proyecto proyecto) {
        try {
            Proyecto actualizado = proyectoService.actualizar(id, proyecto);
            return ResponseEntity.ok(actualizado);
        } catch (ResponseStatusException e) {
            return manejarError(e);
        } catch (Exception e) {
            return errorInterno(e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable int id) {
        try {
            proyectoService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (ResponseStatusException e) {
            return manejarError(e);
        } catch (Exception e) {
            return errorInterno(e);
        }
    }

    private ResponseEntity<?> manejarError(ResponseStatusException e) {
        return ResponseEntity.status(e.getStatusCode())
                .body(e.getReason());
    }

    private ResponseEntity<?> errorInterno(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error interno del servidor: " + e.getMessage());
    }
}
