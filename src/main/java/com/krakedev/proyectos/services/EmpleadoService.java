
package com.krakedev.proyectos.services;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.krakedev.proyectos.entidades.Empleado;
import com.krakedev.proyectos.repositories.EmpleadoRepository;

@Service
public class EmpleadoService {

    private final EmpleadoRepository empleadoRepository;

    public EmpleadoService(EmpleadoRepository empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
    }

    public List<Empleado> listar() {
        return empleadoRepository.findAll();
    }

    public Empleado buscarPorId(int id) {
        return empleadoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Empleado no encontrado"));
    }

    public Empleado crear(Empleado empleado) {
        empleado.setId(0);
        return empleadoRepository.save(empleado);
    }

    public Empleado actualizar(int id, Empleado datos) {
        Empleado empleado = buscarPorId(id);

        empleado.setNombre(datos.getNombre());
        empleado.setCargo(datos.getCargo());

        return empleadoRepository.save(empleado);
    }

    public void eliminar(int id) {
        Empleado empleado = buscarPorId(id);
        empleadoRepository.delete(empleado);
    }
}
