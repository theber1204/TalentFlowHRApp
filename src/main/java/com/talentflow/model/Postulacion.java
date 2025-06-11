package com.talentflow.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Postulacion implements Serializable {
    private Long id;
    private Candidato candidato;
    private String vacante;
    private Date fecha;
    private String estado;
    private Long columnaId;

    public String getFechaFormateada() {
        if (fecha == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(fecha);
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Candidato getCandidato() {
        return candidato;
    }

    public void setCandidato(Candidato candidato) {
        this.candidato = candidato;
    }

    public String getVacante() {
        return vacante;
    }

    public void setVacante(String vacante) {
        this.vacante = vacante;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Long getColumnaId() {
        return columnaId;
    }

    public void setColumnaId(Long columnaId) {
        this.columnaId = columnaId;
    }
}