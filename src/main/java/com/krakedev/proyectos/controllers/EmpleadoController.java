
package com.krakedev.proyectos.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

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
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	public List<Empleado> listar() {
		return empleadoService.listar();
	}

	@GetMapping("/{id}")
	public Empleado buscarPorId(@PathVariable int id) {
		return empleadoService.buscarPorId(id);
	}

	@PostMapping
	public ResponseEntity<?> crear(@RequestBody Empleado empleado) {
		try {
			System.out.println(">>> ENTRÓ AL MÉTODO CREAR EMPLEADO");

			Empleado creado = empleadoService.crear(empleado);

			return ResponseEntity.status(HttpStatus.CREATED).body(creado);

		} catch (Exception e) {
			e.printStackTrace();

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error al crear empleado: " + e.getMessage());
		}
	}

	@PutMapping("/{id}")
	public Empleado actualizar(@PathVariable int id, @RequestBody Empleado empleado) {
		return empleadoService.actualizar(id, empleado);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminar(@PathVariable int id) {
		empleadoService.eliminar(id);
		return ResponseEntity.noContent().build();
	}
}
