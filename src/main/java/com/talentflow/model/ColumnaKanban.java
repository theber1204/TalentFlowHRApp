package com.talentflow.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ColumnaKanban implements Serializable {
    private Long id;
    private String titulo;
    private String color;
    private List<Postulacion> postulaciones;

    public ColumnaKanban(Long id, String titulo, String color, List<Postulacion> postulaciones) {
        this.id = id;
        this.titulo = titulo;
        this.color = color;
        this.postulaciones = postulaciones;
    }
    public ColumnaKanban(long l, String pendiente) {
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<Postulacion> getPostulaciones() {
        return postulaciones;
    }

    public void setPostulaciones(List<Postulacion> postulaciones) {
        this.postulaciones = postulaciones;
    }
}