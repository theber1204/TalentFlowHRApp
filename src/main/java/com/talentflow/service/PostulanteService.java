package com.talentflow.service;

import com.talentflow.model.Postulante;
import java.util.List;

public interface PostulanteService {
    List<Postulante> findAll();

    List<Postulante> findByVacanteId(Long vacanteId);

    Postulante findById(Long id);

    void create(Postulante postulante);

    void update(Postulante postulante);

    void delete(Long id);
}