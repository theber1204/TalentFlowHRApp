package com.talentflow.dao;

import com.talentflow.model.Vacante;
import com.talentflow.utils.DatabaseUtils;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VacanteDAO {

    public List<Vacante> findAll() {
        List<Vacante> vacantes = new ArrayList<>();
        String sql = "SELECT * FROM vacantes ORDER BY fecha_creacion DESC";

        try (Connection conn = DatabaseUtils.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                vacantes.add(mapResultSetToVacante(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vacantes;
    }

    public Vacante findById(Long id) {
        String sql = "SELECT * FROM vacantes WHERE id = ?";

        try (Connection conn = DatabaseUtils.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToVacante(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void create(Vacante vacante) {
        String sql = "INSERT INTO vacantes (titulo, descripcion, requisitos, ubicacion, salario, tipo_contrato, activa, fecha_creacion, fecha_cierre) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtils.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            setVacanteParameters(stmt, vacante);
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    vacante.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Vacante vacante) {
        String sql = "UPDATE vacantes SET titulo = ?, descripcion = ?, requisitos = ?, ubicacion = ?, salario = ?, tipo_contrato = ?, activa = ?, fecha_cierre = ? WHERE id = ?";

        try (Connection conn = DatabaseUtils.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, vacante.getTitulo());
            stmt.setString(2, vacante.getDescripcion());
            stmt.setString(3, vacante.getRequisitos());
            stmt.setString(4, vacante.getUbicacion());
            stmt.setString(5, vacante.getSalario());
            stmt.setString(6, vacante.getTipoContrato());
            stmt.setBoolean(7, vacante.isActiva());
            if (vacante.getFechaCierre() != null) {
                stmt.setTimestamp(8, new Timestamp(vacante.getFechaCierre().getTime()));
            } else {
                stmt.setNull(8, Types.TIMESTAMP);
            }
            stmt.setLong(9, vacante.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(Long id) {
        String sql = "DELETE FROM vacantes WHERE id = ?";

        try (Connection conn = DatabaseUtils.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Vacante mapResultSetToVacante(ResultSet rs) throws SQLException {
        Vacante vacante = new Vacante();
        vacante.setId(rs.getLong("id"));
        vacante.setTitulo(rs.getString("titulo"));
        vacante.setDescripcion(rs.getString("descripcion"));
        vacante.setRequisitos(rs.getString("requisitos"));
        vacante.setUbicacion(rs.getString("ubicacion"));
        vacante.setSalario(rs.getString("salario"));
        vacante.setTipoContrato(rs.getString("tipo_contrato"));
        vacante.setActiva(rs.getBoolean("activa"));
        vacante.setFechaCreacion(rs.getTimestamp("fecha_creacion"));

        Timestamp fechaCierre = rs.getTimestamp("fecha_cierre");
        if (fechaCierre != null) {
            vacante.setFechaCierre(new Date(fechaCierre.getTime()));
        }

        return vacante;
    }

    private void setVacanteParameters(PreparedStatement stmt, Vacante vacante) throws SQLException {
        stmt.setString(1, vacante.getTitulo());
        stmt.setString(2, vacante.getDescripcion());
        stmt.setString(3, vacante.getRequisitos());
        stmt.setString(4, vacante.getUbicacion());
        stmt.setString(5, vacante.getSalario());
        stmt.setString(6, vacante.getTipoContrato());
        stmt.setBoolean(7, vacante.isActiva());
        stmt.setTimestamp(8, new Timestamp(vacante.getFechaCreacion().getTime()));

        if (vacante.getFechaCierre() != null) {
            stmt.setTimestamp(9, new Timestamp(vacante.getFechaCierre().getTime()));
        } else {
            stmt.setNull(9, Types.TIMESTAMP);
        }
    }
}