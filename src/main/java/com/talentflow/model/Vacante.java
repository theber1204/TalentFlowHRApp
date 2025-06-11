package com.talentflow.model;

import jakarta.persistence.*;

@Entity
public class Vacante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String descripcion;

    @Column(name = "responsable_id")
    private Long responsableId;

    private String estado;

    // Getters y setters
}