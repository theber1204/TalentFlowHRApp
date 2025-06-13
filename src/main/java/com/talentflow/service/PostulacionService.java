package com.talentflow.service;

import com.talentflow.model.Candidato;
import com.talentflow.model.ColumnaKanban;
import com.talentflow.model.Comentario;
import com.talentflow.model.Postulacion;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ApplicationScoped
public class PostulacionService {

    public List<Comentario> obtenerComentariosPorPostulacion(Long postulacionId) {
        // Simulación de comentarios para una postulación
        List<Comentario> comentarios = new ArrayList<>();
        comentarios.add(new Comentario(postulacionId, "Usuario1", "Comentario de ejemplo 1", new Date()));
        comentarios.add(new Comentario(postulacionId, "Usuario2", "Comentario de ejemplo 2", new Date()));
        return comentarios;
    }
    public List<ColumnaKanban> obtenerColumnasKanban() {
        // Simulación de columnas Kanban
        List<ColumnaKanban> columnas = new ArrayList<>();
        columnas.add(new ColumnaKanban(1L, "Pendiente"));
        columnas.add(new ColumnaKanban(2L, "En Proceso"));
        columnas.add(new ColumnaKanban(3L, "Finalizado"));
        return columnas;
    }

    public void iniciarProceso(Candidato candidato) {
        // Lógica para iniciar un proceso de postulación
        System.out.println("Iniciando proceso para el candidato: " + candidato.getNombre());
    }

    public void moverPostulacion(Long tarjetaId, Long columnaId) {
        // Lógica para mover una postulación entre columnas
        System.out.println("Moviendo tarjeta con ID " + tarjetaId + " a la columna con ID " + columnaId);
    }

    public void guardarComentario(Comentario comentario) {
        // Lógica para guardar un comentario
        System.out.println("Guardando comentario: " + comentario.getTexto());
    }
}