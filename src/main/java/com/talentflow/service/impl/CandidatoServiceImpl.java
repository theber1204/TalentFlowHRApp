package com.talentflow.service.impl;

import com.talentflow.dao.CandidatoDAO;
import com.talentflow.model.Candidato;
import com.talentflow.service.CandidatoService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import java.util.List;

@Named
@ApplicationScoped
public class CandidatoServiceImpl implements CandidatoService {

    private final CandidatoDAO candidatoDAO = new CandidatoDAO();

    @Override
    public Candidato findById(Long id) {
        return candidatoDAO.findById(id);
    }

    @Override
    public List<Candidato> findAll() {
        return candidatoDAO.findAll();
    }

    @Override
    public void create(Candidato candidato) {
        candidatoDAO.create(candidato);
    }

    @Override
    public void update(Candidato candidato) {
        candidatoDAO.update(candidato);
    }

    @Override
    public void delete(Long id) {
        candidatoDAO.delete(id);
    }

    // Métodos adicionales como `findById`, `update`, `delete`...
}