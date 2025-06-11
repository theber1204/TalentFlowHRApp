package com.talentflow.service;

import com.talentflow.model.Candidato;
import java.util.List;

public interface CandidatoService {
    List<Candidato> findAll();
    Candidato findById(Long id);
    void create(Candidato candidato);
    void update(Candidato candidato);
    void delete(Long id);
}