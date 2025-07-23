package com.talentflow.service.impl;

import com.talentflow.dao.VacanteDAO;
import com.talentflow.model.Vacante;
import com.talentflow.service.VacanteService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import java.util.List;
import java.util.stream.Collectors;

@Named
@ApplicationScoped
public class VacanteServiceImpl implements VacanteService {

    private final VacanteDAO vacanteDAO;

    public VacanteServiceImpl() {
        this.vacanteDAO = new VacanteDAO();
    }

    @Override
    public List<Vacante> findAll() {
        return vacanteDAO.findAll();
    }

    @Override
    public Vacante findById(Long id) {
        return vacanteDAO.findById(id);
    }

    @Override
    public void create(Vacante vacante) {
        vacanteDAO.create(vacante);
    }

    @Override
    public void update(Vacante vacante) {
        vacanteDAO.update(vacante);
    }

    @Override
    public void delete(Long id) {
        vacanteDAO.delete(id);
    }

    @Override
    public List<Vacante> findActivas() {
        return vacanteDAO.findAll().stream()
                .filter(Vacante::isActiva)
                .collect(Collectors.toList());
    }
}