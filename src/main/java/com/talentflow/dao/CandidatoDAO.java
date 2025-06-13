package com.talentflow.dao;

import com.talentflow.model.Candidato;
import com.talentflow.utils.DatabaseUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CandidatoDAO {

    public Candidato findById(Long id) {
        String query = "SELECT * FROM candidatos WHERE id = ?";
        try (ResultSet rs = DatabaseUtils.executeQuery(query, id)) {
            if (rs.next()) {
                Candidato candidato = new Candidato();
                candidato.setId(rs.getLong("id"));
                candidato.setNombre(rs.getString("nombre"));
                candidato.setApellidos(rs.getString("apellidos"));
                candidato.setEmail(rs.getString("email"));
                candidato.setTelefono(rs.getString("telefono"));
                candidato.setEmpresa(rs.getString("empresa"));
                candidato.setEstado(rs.getString("estado"));
                candidato.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
                candidato.setFechaActualizacion(rs.getTimestamp("fecha_actualizacion"));
                return candidato;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener el candidato con ID: " + id, e);
        }
        return null;
    }

    public List<Candidato> findAll() {
        List<Candidato> candidatos = new ArrayList<>();
        String query = "SELECT * FROM candidatos";

        try (ResultSet rs = DatabaseUtils.executeQuery(query)) {
            while (rs.next()) {
                Candidato candidato = new Candidato();
                candidato.setId(rs.getLong("id"));
                candidato.setNombre(rs.getString("nombre"));
                candidato.setApellidos(rs.getString("apellidos"));
                candidato.setEmail(rs.getString("email"));
                candidato.setTelefono(rs.getString("telefono"));
                candidato.setEmpresa(rs.getString("empresa"));
                candidato.setEstado(rs.getString("estado"));
                candidato.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
                candidato.setFechaActualizacion(rs.getTimestamp("fecha_actualizacion"));
                candidatos.add(candidato);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener candidatos", e);
        }

        return candidatos;
    }

    public void create(Candidato candidato) {
        String query = "INSERT INTO candidatos (nombre, apellidos, email, telefono, empresa, estado, fecha_creacion) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            DatabaseUtils.executeUpdate(query, candidato.getNombre(), candidato.getApellidos(), candidato.getEmail(),
                    candidato.getTelefono(), candidato.getEmpresa(), candidato.getEstado(), candidato.getFechaCreacion());
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear el candidato", e);
        }
    }

    public void update(Candidato candidato) {
        String query = "UPDATE candidatos SET nombre = ?, apellidos = ?, email = ?, telefono = ?, empresa = ?, " +
                "estado = ?, fecha_actualizacion = ? WHERE id = ?";
        try {
            DatabaseUtils.executeUpdate(query, candidato.getNombre(), candidato.getApellidos(), candidato.getEmail(),
                    candidato.getTelefono(), candidato.getEmpresa(), candidato.getEstado(),
                    candidato.getFechaActualizacion(), candidato.getId());
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar el candidato con ID: " + candidato.getId(), e);
        }
    }
    public void delete(Long id) {
        String query = "DELETE FROM candidatos WHERE id = ?";
        try {
            DatabaseUtils.executeUpdate(query, id);
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar el candidato con ID: " + id, e);
        }
    }
}