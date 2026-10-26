
package com.krakedev.proyectos.services;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.krakedev.proyectos.entidades.Proyecto;
import com.krakedev.proyectos.repositories.ProyectoRepository;

@Service
public class ProyectoService {

    private final ProyectoRepository proyectoRepository;

    public ProyectoService(ProyectoRepository proyectoRepository) {
        this.proyectoRepository = proyectoRepository;
    }

    public List<Proyecto> listar() {
        return proyectoRepository.findAll();
    }

    public Proyecto buscarPorId(int id) {
        return proyectoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Proyecto no encontrado"));
    }

    public Proyecto crear(Proyecto proyecto) {
        proyecto.setId(null);
        return proyectoRepository.save(proyecto);
    }

    public Proyecto actualizar(int id, Proyecto datos) {
        Proyecto proyecto = buscarPorId(id);

        proyecto.setNombre(datos.getNombre());
        proyecto.setDescripcion(datos.getDescripcion());
        proyecto.setFechaInicio(datos.getFechaInicio());

        return proyectoRepository.save(proyecto);
    }

    public void eliminar(int id) {
        Proyecto proyecto = buscarPorId(id);
        proyectoRepository.delete(proyecto);
    }
}
