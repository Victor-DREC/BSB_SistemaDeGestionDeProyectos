
package com.krakedev.proyectos.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
            return ResponseEntity.ok(tareaService.listar());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al listar las tareas: " + e.getMessage());
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> guardar(@RequestBody Tarea tarea) {
        try {
            Tarea tareaGuardada = tareaService.crear(tarea);
            return ResponseEntity.status(HttpStatus.CREATED).body(tareaGuardada);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode())
                    .body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al guardar la tarea: " + e.getMessage());
        }
    }
    
    @GetMapping("/id")
    public ResponseEntity<?> buscarPorId(int id){
    	try {
    		Tarea tareaB = tareaService.buscarPorId(id);
    		return ResponseEntity.status(HttpStatus.ACCEPTED).body(tareaB);
    	}catch(Exception e) {
    		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
    				.body("Error al tarea buscar por id");
    	}
    }
    
    public ResponseEntity<?> eliminarPorId(int id){
    	tareaService.eliminar(id);
    	return ResponseEntity.noContent().build();
    }
}
