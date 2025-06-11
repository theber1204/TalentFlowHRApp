package com.talentflow.model;

import jakarta.persistence.*;

@Entity
public class Postulante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String email;

    @Column(name = "cv_url")
    private String cvUrl;

    @Column(name = "vacante_id")
    private Long vacanteId;

    @Column(name = "estado_proceso")
    private String estadoProceso;

    // Getters y setters
}