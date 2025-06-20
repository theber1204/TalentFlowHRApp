package com.talentflow.model;

import java.io.Serializable;
import java.util.List;

public class Candidato implements Serializable {
    private Long id;
    private String nombre;
    private String apellidos;
    private String email;
    private String telefono;
    private String empresa;
    private String estado;
    private List<String> archivosAdjuntos;
    private java.util.Date fechaCreacion;
    private java.util.Date fechaActualizacion;

    private String columnaId;

    public Candidato() {}

    public Candidato(Long id, String nombre, String apellidos, String email, String telefono, String empresa, String estado,
                     List<String> archivosAdjuntos, java.util.Date fechaCreacion, java.util.Date fechaActualizacion) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.telefono = telefono;
        this.empresa = empresa;
        this.estado = estado;
        this.archivosAdjuntos = archivosAdjuntos;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
    }

    public <E> Candidato(long l, String juan, String pérez, String mail, String number, String senior, List<E> java) {
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

    public String getEmpresa() {return empresa;}

    public void setEmpresa(String empresa) {this.empresa = empresa;}

    // Getters y Setters para columnaId
    public String getColumnaId() {
        return columnaId;
    }

    public void setColumnaId(String columnaId) {
        this.columnaId = columnaId;
    }

    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public List<String> getArchivosAdjuntos() {
        return archivosAdjuntos;
    }
    public void setArchivosAdjuntos(List<String> archivosAdjuntos) {
        this.archivosAdjuntos = archivosAdjuntos;
    }
    public java.util.Date getFechaCreacion() {
        return fechaCreacion;
    }
    public void setFechaCreacion(java.util.Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    public java.util.Date getFechaActualizacion() {
        return fechaActualizacion;
    }
    public void setFechaActualizacion(java.util.Date fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
}