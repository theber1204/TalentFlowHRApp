package com.talentflow.service;

import com.talentflow.model.Vacante;
import java.util.List;

public interface VacanteService {
    List<Vacante> findAll();

    Vacante findById(Long id);

    void create(Vacante vacante);

    void update(Vacante vacante);

    void delete(Long id);

    List<Vacante> findActivas();
}