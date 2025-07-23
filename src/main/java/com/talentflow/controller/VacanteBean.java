package com.talentflow.controller;

import com.talentflow.model.Vacante;
import com.talentflow.service.ExportService;
import com.talentflow.service.VacanteService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.primefaces.model.StreamedContent;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Named
@ViewScoped
public class VacanteBean implements Serializable {

    @Inject
    private VacanteService vacanteService;

    private List<Vacante> vacantes;
    private List<Vacante> filteredVacantes;
    private Vacante vacante;
    private boolean editMode;
    @Inject
    private ExportService exportService;

    @PostConstruct
    public void init() {
        vacantes = vacanteService.findAll();
        prepareNewVacante();
    }

    public void prepareNewVacante() {
        vacante = new Vacante();
        editMode = false;
    }

    public void editVacante(Vacante vacante) {
        this.vacante = vacante;
        editMode = true;
    }

    public void prepareDeleteVacante(Vacante vacante) {
        this.vacante = vacante;
    }

    public void saveVacante() {
        try {
            if (editMode) {
                vacanteService.update(vacante);
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Vacante actualizada correctamente"));
            } else {
                vacante.setFechaCreacion(new java.util.Date());
                vacante.setActiva(true); // Por defecto activa al crear
                vacanteService.create(vacante);
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Vacante creada correctamente"));
            }

            vacantes = vacanteService.findAll();
            prepareNewVacante();

        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                            "Ha ocurrido un error al guardar: " + e.getMessage()));
        }
    }

    public void deleteVacante() {
        try {
            vacanteService.delete(vacante.getId());
            vacantes = vacanteService.findAll();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Vacante eliminada correctamente"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                            "Ha ocurrido un error al eliminar: " + e.getMessage()));
        }
    }

    // Getters y Setters
    public List<Vacante> getVacantes() {
        return vacantes;
    }

    public void setVacantes(List<Vacante> vacantes) {
        this.vacantes = vacantes;
    }

    public List<Vacante> getFilteredVacantes() {
        return filteredVacantes;
    }

    public void setFilteredVacantes(List<Vacante> filteredVacantes) {
        this.filteredVacantes = filteredVacantes;
    }

    public Vacante getVacante() {
        return vacante;
    }

    public void setVacante(Vacante vacante) {
        this.vacante = vacante;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public StreamedContent getVacantesCSV() {
        List<String> headers = Arrays.asList("Título", "Descripción", "Requisitos", "Ubicación", "Salario", "Tipo de Contrato", "Activa", "Fecha Creación", "Fecha Cierre");
        List<List<String>> rows = new ArrayList<>();
        for (Vacante v : vacantes) {
            rows.add(Arrays.asList(
                    v.getTitulo(),
                    v.getDescripcion(),
                    v.getRequisitos(),
                    v.getUbicacion(),
                    v.getSalario(),
                    v.getTipoContrato(),
                    v.isActiva() ? "Sí" : "No",
                    v.getFechaCreacion() != null ? new SimpleDateFormat("dd/MM/yyyy").format(v.getFechaCreacion()) : "",
                    v.getFechaCierre() != null ? new SimpleDateFormat("dd/MM/yyyy").format(v.getFechaCierre()) : ""
            ));
        }
        return exportService.exportarCSVGenerico(headers, rows, "vacantes");
    }
}