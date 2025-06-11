package com.talentflow.service.impl;

import com.talentflow.model.Candidato;
import com.talentflow.service.CandidatoService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Named
@ApplicationScoped
public class CandidatoServiceImpl implements CandidatoService {

    private final List<Candidato> candidatos = new ArrayList<>();
    private final AtomicLong sequence = new AtomicLong(1);

    public CandidatoServiceImpl() {
        // Datos de prueba
        Candidato c1 = new Candidato();
        c1.setId(sequence.getAndIncrement());
        c1.setNombre("Juan");
        c1.setApellidos("Pérez");
        c1.setEmail("juan@example.com");
        c1.setTelefono("555-1234");
        c1.setEstado("Activo");
        c1.setArchivosAdjuntos(new ArrayList<>());
        c1.setFechaCreacion(new Date());

        Candidato c2 = new Candidato();
        c2.setId(sequence.getAndIncrement());
        c2.setNombre("María");
        c2.setApellidos("López");
        c2.setEmail("maria@example.com");
        c2.setTelefono("555-5678");
        c2.setEstado("Entrevista");
        c2.setArchivosAdjuntos(new ArrayList<>());
        c2.setFechaCreacion(new Date());

        candidatos.add(c1);
        candidatos.add(c2);
    }

    @Override
    public List<Candidato> findAll() {
        return new ArrayList<>(candidatos);
    }

    @Override
    public Candidato findById(Long id) {
        return candidatos.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void create(Candidato candidato) {
        candidato.setId(sequence.getAndIncrement());
        candidato.setFechaCreacion(new Date());
        candidatos.add(candidato);
    }

    @Override
    public void update(Candidato candidato) {
        for (int i = 0; i < candidatos.size(); i++) {
            if (candidatos.get(i).getId().equals(candidato.getId())) {
                candidato.setFechaActualizacion(new Date());
                candidatos.set(i, candidato);
                break;
            }
        }
    }

    @Override
    public void delete(Long id) {
        candidatos.removeIf(c -> c.getId().equals(id));
    }
}