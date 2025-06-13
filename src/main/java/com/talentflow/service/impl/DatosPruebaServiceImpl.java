package com.talentflow.service;

import com.talentflow.model.Candidato;
import com.talentflow.model.Comentario;
import com.talentflow.model.Postulacion;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ApplicationScoped
public class DatosPruebaServiceImpl implements DatosPruebaService {

    @Override
    public List<Postulacion> obtenerPostulacionesPrueba() {
        List<Postulacion> postulaciones = new ArrayList<>();
        Candidato candidato = new Candidato(1L, "Juan", "Pérez", "juan.perez@example.com", "123456789", "Senior", List.of("Java", "Spring"));
        postulaciones.add(new Postulacion(1L, candidato, "Desarrollador Backend", new Date(), "Pendiente"));
        return postulaciones;
    }

    @Override
    public List<Comentario> obtenerComentariosPrueba(Long postulacionId) {
        List<Comentario> comentarios = new ArrayList<>();
        comentarios.add(new Comentario(postulacionId, "Usuario1", "Comentario de prueba 1", new Date()));
        comentarios.add(new Comentario(postulacionId, "Usuario2", "Comentario de prueba 2", new Date()));
        return comentarios;
    }
}