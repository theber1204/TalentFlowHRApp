package com.talentflow.service.impl;

import com.talentflow.dao.PostulanteDAO;
import com.talentflow.model.Postulante;
import com.talentflow.service.PostulanteService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import java.util.List;

@Named
@ApplicationScoped
public class PostulanteServiceImpl implements PostulanteService {

    private final PostulanteDAO postulanteDAO;

    public PostulanteServiceImpl() {
        this.postulanteDAO = new PostulanteDAO();
    }

    @Override
    public List<Postulante> findAll() {
        return postulanteDAO.findAll();
    }

    @Override
    public List<Postulante> findByVacanteId(Long vacanteId) {
        return postulanteDAO.findByVacanteId(vacanteId);
    }

    @Override
    public Postulante findById(Long id) {
        return postulanteDAO.findById(id);
    }

    @Override
    public void create(Postulante postulante) {
        postulanteDAO.create(postulante);
    }

    @Override
    public void update(Postulante postulante) {
        postulanteDAO.update(postulante);
    }

    @Override
    public void delete(Long id) {
        postulanteDAO.delete(id);
    }
}