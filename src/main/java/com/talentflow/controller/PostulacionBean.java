//package com.talentflow.controller;
//
//import com.talentflow.model.Candidato;
//import com.talentflow.model.Comentario;
//import com.talentflow.model.Postulacion;
//import com.talentflow.model.ColumnaKanban;
//import com.talentflow.service.PostulacionService;
//
//import org.primefaces.model.timeline.TimelineEvent;
//import org.primefaces.model.timeline.TimelineModel;
//
//import javax.annotation.PostConstruct;
//import javax.faces.application.FacesMessage;
//import javax.faces.context.FacesContext;
//import javax.faces.view.ViewScoped;
//import javax.inject.Inject;
//import javax.inject.Named;
//import java.io.Serializable;
//import java.time.LocalDateTime;
//import java.util.Date;
//import java.util.List;
//import java.util.ArrayList;
//
//@Named
//@ViewScoped
//public class PostulacionBean implements Serializable {
//
//    @Inject
//    private PostulacionService postulacionService;
//
//    private List<ColumnaKanban> columnas;
//    private Postulacion postulacionSeleccionada;
//    private TimelineModel<Comentario, String> comentariosTimeline;
//    private String nuevoComentario;
//
//    @PostConstruct
//    public void init() {
//        cargarColumnas();
//    }
//
//    private void cargarColumnas() {
//        columnas = postulacionService.obtenerColumnasKanban();
//    }
//
//    public void iniciarProceso(Candidato candidato) {
//        try {
//            postulacionService.iniciarProceso(candidato);
//            FacesContext.getCurrentInstance().addMessage(null,
//                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Proceso iniciado correctamente"));
//
//            // Recargar columnas para reflejar el cambio
//            cargarColumnas();
//        } catch (Exception e) {
//            FacesContext.getCurrentInstance().addMessage(null,
//                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo iniciar el proceso: " + e.getMessage()));
//        }
//    }
//
//    public void moverPostulacion(Long tarjetaId, Long columnaId) {
//        try {
//            postulacionService.moverPostulacion(tarjetaId, columnaId);
//            FacesContext.getCurrentInstance().addMessage(null,
//                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Candidato movido correctamente"));
//        } catch (Exception e) {
//            FacesContext.getCurrentInstance().addMessage(null,
//                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error al mover el candidato: " + e.getMessage()));
//        }
//    }
//
//    public void verDetalles(Postulacion postulacion) {
//        this.postulacionSeleccionada = postulacion;
//    }
//
//    public void verComentarios(Postulacion postulacion) {
//        this.postulacionSeleccionada = postulacion;
//        cargarComentarios();
//    }
//
//    private void cargarComentarios() {
//        List<Comentario> comentarios = postulacionService.obtenerComentarios(postulacionSeleccionada.getId());
//        comentariosTimeline = new TimelineModel<>();
//
//        for (Comentario comentario : comentarios) {
//            comentariosTimeline.add(TimelineEvent.<Comentario>builder()
//                    .data(comentario)
//                    .startDate(comentario.getFecha())
//                    .summary(comentario.getUsuario())
//                    .description(comentario.getTexto())
//                    .build());
//        }
//    }
//
//    public void agregarComentario() {
//        if (nuevoComentario == null || nuevoComentario.trim().isEmpty()) {
//            return;
//        }
//
//        try {
//            Comentario comentario = new Comentario();
//            comentario.setPostulacionId(postulacionSeleccionada.getId());
//            comentario.setTexto(nuevoComentario);
//            comentario.setUsuario("Usuario actual"); // Reemplazar por usuario de sesión
//            comentario.setFecha(new Date());
//
//            postulacionService.guardarComentario(comentario);
//            nuevoComentario = "";
//
//            cargarComentarios();
//        } catch (Exception e) {
//            FacesContext.getCurrentInstance().addMessage(null,
//                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error al guardar comentario: " + e.getMessage()));
//        }
//    }
//
//    public void onAdd(TimelineEvent<Comentario> event) {
//        // Manejar evento de añadir desde la timeline
//    }
//
//    public String getSeverityByExperiencia(String experiencia) {
//        if (experiencia == null) return "info";
//
//        switch (experiencia) {
//            case "Senior": return "success";
//            case "Mid-level": return "warning";
//            case "Junior": return "info";
//            default: return "info";
//        }
//    }
//
//    // Getters y Setters
//
//    public List<ColumnaKanban> getColumnas() {
//        return columnas;
//    }
//
//    public void setColumnas(List<ColumnaKanban> columnas) {
//        this.columnas = columnas;
//    }
//
//    public Postulacion getPostulacionSeleccionada() {
//        return postulacionSeleccionada;
//    }
//
//    public void setPostulacionSeleccionada(Postulacion postulacionSeleccionada) {
//        this.postulacionSeleccionada = postulacionSeleccionada;
//    }
//
//    public TimelineModel<Comentario, String> getComentariosTimeline() {
//        return comentariosTimeline;
//    }
//
//    public void setComentariosTimeline(TimelineModel<Comentario, String> comentariosTimeline) {
//        this.comentariosTimeline = comentariosTimeline;
//    }
//
//    public String getNuevoComentario() {
//        return nuevoComentario;
//    }
//
//    public void setNuevoComentario(String nuevoComentario) {
//        this.nuevoComentario = nuevoComentario;
//    }
//}