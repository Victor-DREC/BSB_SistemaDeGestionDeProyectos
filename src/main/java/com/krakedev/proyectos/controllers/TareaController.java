
package com.krakedev.proyectos.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.krakedev.proyectos.entidades.Tarea;
import com.krakedev.proyectos.services.TareaService;

@RestController
@RequestMapping("/api/tareas")
public class TareaController {

    private final TareaService tareaService;

    public TareaController(TareaService tareaService) {
        this.tareaService = tareaService;
    }

    @GetMapping("/")
    public ResponseEntity<?> listar() {
        try {
            List<Tarea> tareas = tareaService.listar();
            return ResponseEntity.ok(tareas);

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al listar las tareas: " + e.getMessage());
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> guardar(@RequestBody Tarea tarea) {
        try {
            Tarea tareaGuardada = tareaService.crear(tarea);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(tareaGuardada);

        } catch (org.springframework.web.server.ResponseStatusException e) {
            return ResponseEntity
                    .status(e.getStatusCode())
                    .body(e.getReason());

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al guardar la tarea: " + e.getMessage());
        }
    }
}
