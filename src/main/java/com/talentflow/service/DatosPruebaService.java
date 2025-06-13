package com.talentflow.service;

import com.talentflow.model.Comentario;
import com.talentflow.model.Postulacion;

import java.util.List;

public interface DatosPruebaService {
    List<Postulacion> obtenerPostulacionesPrueba();
    List<Comentario> obtenerComentariosPrueba(Long postulacionId);
}