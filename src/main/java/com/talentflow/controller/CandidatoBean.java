package com.talentflow.controller;

import com.talentflow.model.Candidato;
import com.talentflow.service.CandidatoService;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.primefaces.model.file.UploadedFile;

@Named
@ViewScoped
public class CandidatoBean implements Serializable {

    @Inject
    private CandidatoService candidatoService;

    private List<Candidato> candidatos;
    private List<Candidato> filteredCandidatos;
    private Candidato candidato;
    private boolean editMode;
    private UploadedFile archivoAdjunto;

    @PostConstruct
    public void init() {
        // Inicializar la lista de candidatos
        candidatos = candidatoService.findAll();
        prepareNewCandidato();
    }

    public void prepareNewCandidato() {
        candidato = new Candidato();
        candidato.setArchivosAdjuntos(new ArrayList<>());
        editMode = false;
    }

    public void editCandidato(Candidato candidato) {
        this.candidato = candidato;
        editMode = true;
    }

    public void prepareDeleteCandidato(Candidato candidato) {
        this.candidato = candidato;
    }

    public void saveCandidato() {
        try {
            if (archivoAdjunto != null && archivoAdjunto.getSize() > 0) {
                String rutaArchivo = guardarArchivo(archivoAdjunto);
                if (candidato.getArchivosAdjuntos() == null) {
                    candidato.setArchivosAdjuntos(new ArrayList<>());
                }
                candidato.getArchivosAdjuntos().add(rutaArchivo);
            }

            if (editMode) {
                candidatoService.update(candidato);
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Candidato actualizado correctamente"));
            } else {
                candidatoService.create(candidato);
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Candidato creado correctamente"));
            }

            candidatos = candidatoService.findAll();
            prepareNewCandidato();

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                            "Ha ocurrido un error al guardar: " + e.getMessage()));
        }
    }

    public void deleteCandidato() {
        try {
            candidatoService.delete(candidato.getId());
            candidatos = candidatoService.findAll();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Candidato eliminado correctamente"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                            "Ha ocurrido un error al eliminar: " + e.getMessage()));
        }
    }

    private String guardarArchivo(UploadedFile archivo) {
        // Implementación para guardar archivo
        String nombreArchivo = System.currentTimeMillis() + "_" + archivo.getFileName();
        String ruta = "/uploads/cv/" + nombreArchivo;

        // Aquí iría el código para guardar físicamente el archivo
        // Por ejemplo:
        // try (InputStream input = archivo.getInputStream();
        //      OutputStream output = new FileOutputStream(new File("/ruta/fisica/" + nombreArchivo))) {
        //     IOUtils.copy(input, output);
        // } catch (IOException e) {
        //     throw new RuntimeException("Error al guardar el archivo", e);
        // }

        return ruta;
    }
    // Getters y Setters

    public List<Candidato> getCandidatos() {
        return candidatos;
    }

    public void setCandidatos(List<Candidato> candidatos) {
        this.candidatos = candidatos;
    }

    public List<Candidato> getFilteredCandidatos() {
        return filteredCandidatos;
    }

    public void setFilteredCandidatos(List<Candidato> filteredCandidatos) {
        this.filteredCandidatos = filteredCandidatos;
    }

    public Candidato getCandidato() {
        return candidato;
    }

    public void setCandidato(Candidato candidato) {
        this.candidato = candidato;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public UploadedFile getArchivoAdjunto() {
        return archivoAdjunto;
    }

    public void setArchivoAdjunto(UploadedFile archivoAdjunto) {
        this.archivoAdjunto = archivoAdjunto;
    }
}