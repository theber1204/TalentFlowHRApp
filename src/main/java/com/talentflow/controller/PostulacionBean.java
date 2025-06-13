package com.talentflow.controller;

import com.talentflow.model.Candidato;
import com.talentflow.model.Comentario;
import com.talentflow.model.Postulacion;
import com.talentflow.model.ColumnaKanban;
import com.talentflow.service.PostulacionService;

import org.primefaces.model.timeline.TimelineEvent;
import org.primefaces.model.timeline.TimelineModel;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

@Named
@ViewScoped
public class PostulacionBean implements Serializable {

    private static final String USUARIO_ACTUAL = "Usuario actual"; // Reemplazar con lógica de sesión

    @Inject
    private PostulacionService postulacionService;

    private List<ColumnaKanban> columnas;
    private Postulacion postulacionSeleccionada;
    private TimelineModel<Comentario, String> comentariosTimeline;
    private String nuevoComentario;

    @PostConstruct
    public void init() {
        try {
            cargarColumnas();
        } catch (Exception e) {
            mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Error", "No se pudieron cargar las columnas: " + e.getMessage());
        }
    }

    private void cargarColumnas() {
        if (postulacionService == null) {
            throw new IllegalStateException("El servicio PostulacionService no está disponible.");
        }
        columnas = postulacionService.obtenerColumnasKanban();
    }

    public void iniciarProceso(Candidato candidato) {
        if (candidato == null) {
            mostrarMensaje(FacesMessage.SEVERITY_WARN, "Advertencia", "El candidato no puede ser nulo.");
            return;
        }

        try {
            postulacionService.iniciarProceso(candidato);
            mostrarMensaje(FacesMessage.SEVERITY_INFO, "Éxito", "Proceso iniciado correctamente");
            cargarColumnas();
        } catch (Exception e) {
            mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo iniciar el proceso: " + e.getMessage());
        }
    }

    public void moverPostulacion(Long tarjetaId, Long columnaId) {
        if (tarjetaId == null || columnaId == null) {
            mostrarMensaje(FacesMessage.SEVERITY_WARN, "Advertencia", "La tarjeta o columna no puede ser nula.");
            return;
        }

        try {
            postulacionService.moverPostulacion(tarjetaId, columnaId);
            mostrarMensaje(FacesMessage.SEVERITY_INFO, "Éxito", "Candidato movido correctamente");
        } catch (Exception e) {
            mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Error", "Error al mover el candidato: " + e.getMessage());
        }
    }

    public void verDetalles(Postulacion postulacion) {
        if (postulacion == null) {
            mostrarMensaje(FacesMessage.SEVERITY_WARN, "Advertencia", "La postulación no puede ser nula.");
            return;
        }
        this.postulacionSeleccionada = postulacion;
    }

    public void verComentarios(Postulacion postulacion) {
        if (postulacion == null) {
            mostrarMensaje(FacesMessage.SEVERITY_WARN, "Advertencia", "La postulación no puede ser nula.");
            return;
        }

        this.postulacionSeleccionada = postulacion;
        cargarComentarios();
    }

    private void cargarComentarios() {
        if (postulacionSeleccionada == null) {
            mostrarMensaje(FacesMessage.SEVERITY_WARN, "Advertencia", "No hay una postulación seleccionada.");
            return;
        }

        try {
            List<Comentario> comentarios = postulacionService.obtenerComentariosPorPostulacion(postulacionSeleccionada.getId());
            comentariosTimeline = new TimelineModel<>();

            for (Comentario comentario : comentarios) {
                TimelineEvent<Comentario> evento = TimelineEvent.<Comentario>builder()
                        .data(comentario)
                        .startDate(comentario.getFecha().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime())
                        .build();
                comentariosTimeline.add(evento);
            }
        } catch (Exception e) {
            mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Error", "Error al cargar los comentarios: " + e.getMessage());
        }
    }

    public void agregarComentario() {
        if (nuevoComentario == null || nuevoComentario.trim().isEmpty()) {
            mostrarMensaje(FacesMessage.SEVERITY_WARN, "Advertencia", "El comentario no puede estar vacío.");
            return;
        }

        try {
            Comentario comentario = new Comentario();
            comentario.setPostulacionId(postulacionSeleccionada.getId());
            comentario.setTexto(nuevoComentario);
            comentario.setUsuario(USUARIO_ACTUAL);
            comentario.setFecha(new Date());

            postulacionService.guardarComentario(comentario);
            nuevoComentario = "";
            cargarComentarios();
        } catch (Exception e) {
            mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Error", "Error al guardar comentario: " + e.getMessage());
        }
    }

    public String getSeverityByExperiencia(String experiencia) {
        if (experiencia == null) return "info";

        switch (experiencia) {
            case "Senior": return "success";
            case "Mid-level": return "warning";
            case "Junior": return "info";
            default: return "info";
        }
    }

    private void mostrarMensaje(FacesMessage.Severity severity, String resumen, String detalle) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, resumen, detalle));
    }

    // Getters y Setters

    public List<ColumnaKanban> getColumnas() {
        return columnas;
    }

    public void setColumnas(List<ColumnaKanban> columnas) {
        this.columnas = columnas;
    }

    public Postulacion getPostulacionSeleccionada() {
        return postulacionSeleccionada;
    }

    public void setPostulacionSeleccionada(Postulacion postulacionSeleccionada) {
        this.postulacionSeleccionada = postulacionSeleccionada;
    }

    public TimelineModel<Comentario, String> getComentariosTimeline() {
        return comentariosTimeline;
    }

    public void setComentariosTimeline(TimelineModel<Comentario, String> comentariosTimeline) {
        this.comentariosTimeline = comentariosTimeline;
    }

    public String getNuevoComentario() {
        return nuevoComentario;
    }

    public void setNuevoComentario(String nuevoComentario) {
        this.nuevoComentario = nuevoComentario;
    }
}