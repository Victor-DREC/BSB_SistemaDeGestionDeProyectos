
package com.krakedev.proyectos.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.krakedev.proyectos.entidades.Empleado;
import com.krakedev.proyectos.entidades.Proyecto;
import com.krakedev.proyectos.entidades.Tarea;
import com.krakedev.proyectos.repositories.EmpleadoRepository;
import com.krakedev.proyectos.repositories.ProyectoRepository;
import com.krakedev.proyectos.repositories.TareaRepository;

@Service
public class TareaService {

    private final TareaRepository tareaRepository;
    private final ProyectoRepository proyectoRepository;
    private final EmpleadoRepository empleadoRepository;

    public TareaService(
            TareaRepository tareaRepository,
            ProyectoRepository proyectoRepository,
            EmpleadoRepository empleadoRepository) {
        this.tareaRepository = tareaRepository;
        this.proyectoRepository = proyectoRepository;
        this.empleadoRepository = empleadoRepository;
    }

    public List<Tarea> listar() {
        return tareaRepository.findAll();
    }

    public Tarea buscarPorId(int id) {
        return tareaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Tarea no encontrada"));
    }

    @Transactional
    public Tarea crear(Tarea tarea) {
        tarea.setId(0);
        prepararRelaciones(tarea);
        return tareaRepository.save(tarea);
    }

    @Transactional
    public Tarea actualizar(int id, Tarea datos) {
        Tarea tarea = buscarPorId(id);

        tarea.setDescripcion(datos.getDescripcion());
        tarea.setFechaLimite(datos.getFechaLimite());
        tarea.setCostoEstimado(datos.getCostoEstimado());

        prepararRelaciones(datos);
        tarea.setProyecto(datos.getProyecto());
        tarea.setEmpleados(datos.getEmpleados());

        return tareaRepository.save(tarea);
    }

    private void prepararRelaciones(Tarea tarea) {
        if (tarea.getProyecto() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Debe indicar el proyecto de la tarea");
        }

        int proyectoId = tarea.getProyecto().getId();

        Proyecto proyecto = proyectoRepository.findById(proyectoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "El proyecto con ID " + proyectoId + " no existe"));

        tarea.setProyecto(proyecto);

        if (tarea.getEmpleados() != null) {
            List<Empleado> empleados = tarea.getEmpleados().stream()
                    .map(e -> empleadoRepository.findById(e.getId())
                            .orElseThrow(() -> new ResponseStatusException(
                                    HttpStatus.NOT_FOUND,
                                    "El empleado con ID " + e.getId()
                                            + " no existe")))
                    .collect(Collectors.toList());

            tarea.setEmpleados(empleados);
        }
    }

    public void eliminar(int id) {
        Tarea tarea = buscarPorId(id);
        tareaRepository.delete(tarea);
    }
}
