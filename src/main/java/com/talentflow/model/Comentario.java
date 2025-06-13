package com.talentflow.model;

import java.io.Serializable;
import java.util.Date;

public class Comentario implements Serializable {
    private Long postulacionId;
    private String usuario;
    private String texto;
    private Date fecha;

    public Comentario() {}

    public Comentario(Long postulacionId, String usuario, String texto, Date fecha) {
        this.postulacionId = postulacionId;
        this.usuario = usuario;
        this.texto = texto;
        this.fecha = fecha;
    }

    // Getters y Setters
    public Long getPostulacionId() {
        return postulacionId;
    }

    public void setPostulacionId(Long postulacionId) {
        this.postulacionId = postulacionId;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}