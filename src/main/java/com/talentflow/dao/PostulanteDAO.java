package com.talentflow.dao;

import com.talentflow.model.Postulante;
import com.talentflow.model.Vacante;
import com.talentflow.utils.DatabaseUtils;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostulanteDAO {

    private final VacanteDAO vacanteDAO;

    public PostulanteDAO() {
        this.vacanteDAO = new VacanteDAO();
    }

    public List<Postulante> findAll() {
        List<Postulante> postulantes = new ArrayList<>();
        String sql = "SELECT * FROM postulantes ORDER BY fecha_postulacion DESC";

        try (Connection conn = DatabaseUtils.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                postulantes.add(mapResultSetToPostulante(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return postulantes;
    }

    public List<Postulante> findByVacanteId(Long vacanteId) {
        List<Postulante> postulantes = new ArrayList<>();
        String sql = "SELECT * FROM postulantes WHERE vacante_id = ? ORDER BY fecha_postulacion DESC";

        try (Connection conn = DatabaseUtils.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, vacanteId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    postulantes.add(mapResultSetToPostulante(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return postulantes;
    }

    public Postulante findById(Long id) {
        String sql = "SELECT * FROM postulantes WHERE id = ?";

        try (Connection conn = DatabaseUtils.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPostulante(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void create(Postulante postulante) {
        String sql = "INSERT INTO postulantes (nombre, apellidos, email, telefono, cv, carta_presentacion, " +
                "vacante_id, estado, fecha_postulacion) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtils.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            setPostulanteParameters(stmt, postulante);
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    postulante.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Postulante postulante) {
        String sql = "UPDATE postulantes SET nombre = ?, apellidos = ?, email = ?, telefono = ?, " +
                "cv = ?, carta_presentacion = ?, estado = ? WHERE id = ?";

        try (Connection conn = DatabaseUtils.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, postulante.getNombre());
            stmt.setString(2, postulante.getApellidos());
            stmt.setString(3, postulante.getEmail());
            stmt.setString(4, postulante.getTelefono());
            stmt.setString(5, postulante.getCv());
            stmt.setString(6, postulante.getCartaPresentacion());
            stmt.setString(7, postulante.getEstado());
            stmt.setLong(8, postulante.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(Long id) {
        String sql = "DELETE FROM postulantes WHERE id = ?";

        try (Connection conn = DatabaseUtils.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Postulante mapResultSetToPostulante(ResultSet rs) throws SQLException {
        Postulante postulante = new Postulante();
        postulante.setId(rs.getLong("id"));
        postulante.setNombre(rs.getString("nombre"));
        postulante.setApellidos(rs.getString("apellidos"));
        postulante.setEmail(rs.getString("email"));
        postulante.setTelefono(rs.getString("telefono"));
        postulante.setCv(rs.getString("cv"));
        postulante.setCartaPresentacion(rs.getString("carta_presentacion"));
        postulante.setVacanteId(rs.getLong("vacante_id"));
        postulante.setEstado(rs.getString("estado"));
        postulante.setFechaPostulacion(rs.getTimestamp("fecha_postulacion"));

        // Cargar la vacante relacionada
        Vacante vacante = vacanteDAO.findById(postulante.getVacanteId());
        postulante.setVacante(vacante);

        return postulante;
    }

    private void setPostulanteParameters(PreparedStatement stmt, Postulante postulante) throws SQLException {
        stmt.setString(1, postulante.getNombre());
        stmt.setString(2, postulante.getApellidos());
        stmt.setString(3, postulante.getEmail());
        stmt.setString(4, postulante.getTelefono());
        stmt.setString(5, postulante.getCv());
        stmt.setString(6, postulante.getCartaPresentacion());
        stmt.setLong(7, postulante.getVacanteId());
        stmt.setString(8, postulante.getEstado());
        stmt.setTimestamp(9, new Timestamp(postulante.getFechaPostulacion().getTime()));
    }
}