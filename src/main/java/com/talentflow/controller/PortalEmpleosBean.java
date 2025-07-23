package com.talentflow.controller;

import com.talentflow.model.Postulante;
import com.talentflow.model.Vacante;
import com.talentflow.service.PostulanteService;
import com.talentflow.service.VacanteService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class PortalEmpleosBean implements Serializable {

    @Inject
    private VacanteService vacanteService;

    @Inject
    private PostulanteService postulanteService;

    private List<Vacante> vacantes;
    private Vacante vacanteSeleccionada;
    private Postulante postulante;

    @PostConstruct
    public void init() {
        vacantes = vacanteService.findActivas();
        prepareNewPostulante();
    }

    public void prepareNewPostulante() {
        postulante = new Postulante();
    }

    public void preparePostular(Vacante vacante) {
        this.vacanteSeleccionada = vacante;
        prepareNewPostulante();
    }

    public void postular() {
        try {
            postulante.setVacante(vacanteSeleccionada);
            postulanteService.create(postulante);

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "¡Éxito!",
                            "Tu postulación ha sido enviada correctamente."));

            prepareNewPostulante();

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                            "Ha ocurrido un error al enviar tu postulación: " + e.getMessage()));
        }
    }

    // Getters y Setters
    public List<Vacante> getVacantes() {
        return vacantes;
    }

    public void setVacantes(List<Vacante> vacantes) {
        this.vacantes = vacantes;
    }

    public Vacante getVacanteSeleccionada() {
        return vacanteSeleccionada;
    }

    public void setVacanteSeleccionada(Vacante vacanteSeleccionada) {
        this.vacanteSeleccionada = vacanteSeleccionada;
    }

    public Postulante getPostulante() {
        return postulante;
    }

    public void setPostulante(Postulante postulante) {
        this.postulante = postulante;
    }
}