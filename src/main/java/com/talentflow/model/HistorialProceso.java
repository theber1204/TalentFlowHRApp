package com.talentflow.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class HistorialProceso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "postulante_id")
    private Long postulanteId;

    private String etapa;
    private LocalDate fecha;

    // Getters y setters
}