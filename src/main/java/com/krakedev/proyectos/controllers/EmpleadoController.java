
package com.krakedev.proyectos.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.krakedev.proyectos.entidades.Empleado;
import com.krakedev.proyectos.services.EmpleadoService;

@RestController
@RequestMapping("/api/empleados")
public class EmpleadoController {

    private final EmpleadoService empleadoService;

    public EmpleadoController(EmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
    }

    @GetMapping
    public ResponseEntity<?> listar() {
        try {
            List<Empleado> empleados = empleadoService.listar();
            return ResponseEntity.ok(empleados);
        } catch (Exception e) {
            return errorInterno(e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable int id) {
        try {
            Empleado empleado = empleadoService.buscarPorId(id);
            return ResponseEntity.ok(empleado);
        } catch (ResponseStatusException e) {
            return manejarError(e);
        } catch (Exception e) {
            return errorInterno(e);
        }
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Empleado empleado) {
        try {
            Empleado creado = empleadoService.crear(empleado);
            return ResponseEntity.status(HttpStatus.CREATED).body(creado);
        } catch (Exception e) {
            return errorInterno(e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @PathVariable int id,
            @RequestBody Empleado empleado) {
        try {
            Empleado actualizado = empleadoService.actualizar(id, empleado);
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
            empleadoService.eliminar(id);
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
