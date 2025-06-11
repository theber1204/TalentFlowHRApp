package com.talentflow.repository;

import com.talentflow.model.Usuario;
import com.talentflow.utils.DatabaseUtils;

import jakarta.enterprise.context.ApplicationScoped;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class UsuarioRepository {

    public Usuario findByUsernameAndPassword(String username, String password) throws SQLException {
        String query = "SELECT * FROM usuarios WHERE email = ? AND password = ?";

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUsuario(rs);
                }
            }
        }
        return null;
    }

    public Usuario buscarPorEmail(String email) throws SQLException {
        String query = "SELECT * FROM usuarios WHERE email = ?";

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUsuario(rs);
                }
            }
        }
        return null;
    }

    public Usuario buscarPorId(Long id) throws SQLException {
        String query = "SELECT * FROM usuarios WHERE id = ?";

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUsuario(rs);
                }
            }
        }
        return null;
    }

    public List<Usuario> listarTodos() throws SQLException {
        String query = "SELECT * FROM usuarios ORDER BY fecha_creacion DESC";
        List<Usuario> usuarios = new ArrayList<>();

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                usuarios.add(mapResultSetToUsuario(rs));
            }
        }
        return usuarios;
    }

    // Metodo save en UsuarioRepository
    public boolean save(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuarios (nombre, apellidos, email, password, telefono, empresa, rol, activo, fecha_creacion, fecha_ultimo_acceso) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getApellidos());
            ps.setString(3, usuario.getEmail());
            ps.setString(4, usuario.getPassword());
            ps.setString(5, usuario.getTelefono());
            ps.setString(6, usuario.getEmpresa());
            ps.setString(7, usuario.getRol().name());
            ps.setBoolean(8, usuario.getActivo());
            ps.setObject(9, usuario.getFechaCreacion());
            ps.setObject(10, usuario.getFechaUltimoAcceso());
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                System.out.println("Error al insertar el usuario: No se afectaron filas");
                return false;
            }
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    usuario.setId(generatedKeys.getLong(1));
                }
            }
            return true;
        }
    }

    public boolean actualizar(Usuario usuario) throws SQLException {
        String query = """
            UPDATE usuarios SET 
                nombre = ?, apellidos = ?, email = ?, telefono = ?, empresa = ?, 
                rol = ?, activo = ?, fecha_ultimo_acceso = ? 
            WHERE id = ?
            """;

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getApellidos());
            stmt.setString(3, usuario.getEmail());
            stmt.setString(4, usuario.getTelefono());
            stmt.setString(5, usuario.getEmpresa());
            stmt.setString(6, usuario.getRol().name());
            stmt.setBoolean(7, usuario.getActivo());
            stmt.setTimestamp(8, usuario.getFechaUltimoAcceso() != null ?
                    Timestamp.valueOf(usuario.getFechaUltimoAcceso()) : null);
            stmt.setLong(9, usuario.getId());

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean actualizarPassword(Long usuarioId, String nuevaPassword) throws SQLException {
        String query = "UPDATE usuarios SET password = ? WHERE id = ?";

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, nuevaPassword);
            stmt.setLong(2, usuarioId);

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean eliminar(Long id) throws SQLException {
        String query = "DELETE FROM usuarios WHERE id = ?";

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean existsByUsername(String username) throws SQLException {
        String query = "SELECT COUNT(*) FROM usuarios WHERE email = ?";

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public boolean existsByEmail(String email) throws SQLException {
        String query = "SELECT COUNT(*) FROM usuarios WHERE email = ?";

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public List<Usuario> buscarPorRol(Usuario.RolUsuario rol) throws SQLException {
        String query = "SELECT * FROM usuarios WHERE rol = ? AND activo = true ORDER BY nombre";
        List<Usuario> usuarios = new ArrayList<>();

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, rol.name());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    usuarios.add(mapResultSetToUsuario(rs));
                }
            }
        }
        return usuarios;
    }

    public long contarUsuarios() throws SQLException {
        String query = "SELECT COUNT(*) FROM usuarios";

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getLong(1);
            }
        }
        return 0;
    }

    public long contarUsuariosActivos() throws SQLException {
        String query = "SELECT COUNT(*) FROM usuarios WHERE activo = true";

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getLong(1);
            }
        }
        return 0;
    }

    // Método auxiliar para mapear ResultSet a Usuario
    private Usuario mapResultSetToUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(rs.getLong("id"));
        usuario.setNombre(rs.getString("nombre"));
        usuario.setApellidos(rs.getString("apellidos"));
        usuario.setEmail(rs.getString("email"));
        usuario.setPassword(rs.getString("password"));
        usuario.setTelefono(rs.getString("telefono"));
        usuario.setEmpresa(rs.getString("empresa"));

        // Manejar rol con valor por defecto
        String rolStr = rs.getString("rol");
        if (rolStr != null) {
            try {
                usuario.setRol(Usuario.RolUsuario.valueOf(rolStr));
            } catch (IllegalArgumentException e) {
                usuario.setRol(Usuario.RolUsuario.USER);
            }
        } else {
            usuario.setRol(Usuario.RolUsuario.USER);
        }

        usuario.setActivo(rs.getBoolean("activo"));

        // Manejar fechas
        Timestamp fechaCreacion = rs.getTimestamp("fecha_creacion");
        if (fechaCreacion != null) {
            usuario.setFechaCreacion(fechaCreacion.toLocalDateTime());
        }

        Timestamp fechaUltimoAcceso = rs.getTimestamp("fecha_ultimo_acceso");
        if (fechaUltimoAcceso != null) {
            usuario.setFechaUltimoAcceso(fechaUltimoAcceso.toLocalDateTime());
        }

        return usuario;
    }
}