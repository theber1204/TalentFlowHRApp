package com.talentflow.model;

import java.io.Serializable;
import java.util.Date;

public class Postulante implements Serializable {
    private Long id;
    private String nombre;
    private String apellidos;
    private String email;
    private String telefono;
    private String cv;
    private String cartaPresentacion;
    private Long vacanteId;
    private String estado;
    private Date fechaPostulacion;
    private Vacante vacante; // Para relación con la vacante

    public Postulante() {
        this.estado = "PENDIENTE";
        this.fechaPostulacion = new Date();
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCv() {
        return cv;
    }

    public void setCv(String cv) {
        this.cv = cv;
    }

    public String getCartaPresentacion() {
        return cartaPresentacion;
    }

    public void setCartaPresentacion(String cartaPresentacion) {
        this.cartaPresentacion = cartaPresentacion;
    }

    public Long getVacanteId() {
        return vacanteId;
    }

    public void setVacanteId(Long vacanteId) {
        this.vacanteId = vacanteId;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFechaPostulacion() {
        return fechaPostulacion;
    }

    public void setFechaPostulacion(Date fechaPostulacion) {
        this.fechaPostulacion = fechaPostulacion;
    }

    public Vacante getVacante() {
        return vacante;
    }

    public void setVacante(Vacante vacante) {
        this.vacante = vacante;
        if (vacante != null) {
            this.vacanteId = vacante.getId();
        }
    }
}