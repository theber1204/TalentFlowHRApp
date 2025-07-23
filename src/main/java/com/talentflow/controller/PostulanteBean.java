package com.talentflow.controller;

import com.talentflow.dao.PostulanteDAO;
import com.talentflow.model.Postulante;
import com.talentflow.service.ExportService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.primefaces.model.StreamedContent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Named
@ViewScoped
public class PostulanteBean implements Serializable {
    private PostulanteDAO postulanteDAO = new PostulanteDAO();
    private List<Postulante> postulantes;
    private List<Postulante> filteredPostulantes;
    private Postulante postulanteSeleccionado;

    @Inject
    private ExportService exportService;

    @PostConstruct
    public void init() {
        cargarPostulantes();
    }

    public void cargarPostulantes() {
        postulantes = postulanteDAO.findAll();
        filteredPostulantes = new ArrayList<>(postulantes);
    }

    public void savePostulante() {
        if (postulanteSeleccionado != null) {
            if (postulanteSeleccionado.getId() == null) {
                postulanteDAO.create(postulanteSeleccionado);
                mostrarMensaje(FacesMessage.SEVERITY_INFO, "Postulante creado", "Se ha creado el postulante correctamente.");
            } else {
                postulanteDAO.update(postulanteSeleccionado);
                mostrarMensaje(FacesMessage.SEVERITY_INFO, "Postulante actualizado", "Se ha actualizado el postulante correctamente.");
            }
            cargarPostulantes();
        }
    }

    public void deletePostulante() {
        if (postulanteSeleccionado != null && postulanteSeleccionado.getId() != null) {
            postulanteDAO.delete(postulanteSeleccionado.getId());
            mostrarMensaje(FacesMessage.SEVERITY_INFO, "Postulante eliminado", "Se ha eliminado el postulante correctamente.");
            cargarPostulantes();
        }
    }

    public void mostrarMensaje(FacesMessage.Severity severity, String resumen, String detalle) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, resumen, detalle));
    }

    // Getters y Setters
    public List<Postulante> getPostulantes() {
        return postulantes;
    }

    public void setPostulantes(List<Postulante> postulantes) {
        this.postulantes = postulantes;
    }

    public List<Postulante> getFilteredPostulantes() {
        return filteredPostulantes;
    }

    public void setFilteredPostulantes(List<Postulante> filteredPostulantes) {
        this.filteredPostulantes = filteredPostulantes;
    }

    public Postulante getPostulanteSeleccionado() {
        return postulanteSeleccionado;
    }

    public void setPostulanteSeleccionado(Postulante postulanteSeleccionado) {
        this.postulanteSeleccionado = postulanteSeleccionado;
    }

    public StreamedContent getPostulantesCSV() {
        List<String> headers = List.of("ID", "Nombre", "Apellidos", "Email", "Teléfono", "CV", "Carta de Presentación", "Estado", "Fecha de Postulación", "Vacante");
        List<List<String>> data = new ArrayList<>();
        for (Postulante postulante : postulantes) {
            data.add(Arrays.asList(
                    String.valueOf(postulante.getId()),
                    postulante.getNombre(),
                    postulante.getApellidos(),
                    postulante.getEmail(),
                    postulante.getTelefono(),
                    postulante.getCv() != null ? postulante.getCv() : "",
                    postulante.getCartaPresentacion() != null ? postulante.getCartaPresentacion() : "",
                    postulante.getEstado(),
                    postulante.getFechaPostulacion() != null ? postulante.getFechaPostulacion().toString() : "",
                    postulante.getVacante() != null ? postulante.getVacante().getTitulo() : ""
            ));
        }
        return exportService.exportarCSVGenerico(headers, data, "postulantes");
    }
}
