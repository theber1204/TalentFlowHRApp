package com.talentflow.service;

import com.talentflow.model.Usuario;
import com.talentflow.repository.UsuarioRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;

@ApplicationScoped
public class UsuarioService {

    @Inject
    private UsuarioRepository usuarioRepository;

    @Transactional
    public Usuario crearUsuario(Usuario usuario) {
        try {
            System.out.println("UsuarioService: creando usuario " + usuario.getEmail());

            // Validar que el email no exista
            if (existeEmail(usuario.getEmail())) {
                System.err.println("UsuarioService: email ya existe " + usuario.getEmail());
                throw new RuntimeException("El email ya está registrado");
            }

            // Encriptar la contraseña
            String passwordEncriptada = encriptarPassword(usuario.getPassword());
            usuario.setPassword(passwordEncriptada);

            // Establecer valores por defecto si es necesario
            if (usuario.getActivo() == null) {
                usuario.setActivo(true);
            }

            if (usuario.getRol() == null) {
                usuario.setRol(Usuario.RolUsuario.USER);
            }

            // Guardar el usuario
            System.out.println("UsuarioService: guardando usuario en BD");
            boolean guardado = usuarioRepository.save(usuario);

            if (guardado) {
                System.out.println("UsuarioService: usuario guardado con ID " + usuario.getId());
                return usuario;
            } else {
                System.err.println("UsuarioService: error al guardar usuario");
                return null;
            }

        } catch (SQLException e) {
            System.err.println("UsuarioService SQLException: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al crear usuario: " + e.getMessage(), e);
        } catch (Exception e) {
            System.err.println("UsuarioService Exception: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al crear usuario: " + e.getMessage(), e);
        }
    }

    public Usuario autenticar(String email, String password) {
        try {
            // Buscar usuario por email primero
            Usuario usuario = usuarioRepository.buscarPorEmail(email);

            if (usuario != null && usuario.getActivo()) {
                // Verificar la contraseña
                String passwordEncriptada = encriptarPassword(password);
                if (passwordEncriptada.equals(usuario.getPassword())) {
                    // Actualizar último acceso
                    usuario.actualizarUltimoAcceso();
                    usuarioRepository.actualizar(usuario);
                    return usuario;
                }
            }
            return null;

        } catch (SQLException e) {
            throw new RuntimeException("Error en autenticación: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error en autenticación: " + e.getMessage(), e);
        }
    }

    public boolean existeEmail(String email) {
        try {
            return usuarioRepository.existsByEmail(email);
        } catch (SQLException e) {
            return false;
        }
    }

    public Usuario buscarPorId(Long id) {
        try {
            return usuarioRepository.buscarPorId(id);
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar usuario por ID: " + e.getMessage(), e);
        }
    }

    public Usuario buscarPorEmail(String email) {
        try {
            return usuarioRepository.buscarPorEmail(email);
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar usuario por email: " + e.getMessage(), e);
        }
    }

    public List<Usuario> listarTodos() {
        try {
            return usuarioRepository.listarTodos();
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar usuarios: " + e.getMessage(), e);
        }
    }

    public List<Usuario> buscarPorRol(Usuario.RolUsuario rol) {
        try {
            return usuarioRepository.buscarPorRol(rol);
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar usuarios por rol: " + e.getMessage(), e);
        }
    }

    @Transactional
    public Usuario actualizarUsuario(Usuario usuario) {
        try {
            boolean actualizado = usuarioRepository.actualizar(usuario);
            return actualizado ? usuario : null;
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar usuario: " + e.getMessage(), e);
        }
    }

    @Transactional
    public boolean eliminarUsuario(Long id) {
        try {
            Usuario usuario = buscarPorId(id);
            if (usuario != null) {
                // Eliminación lógica
                usuario.setActivo(false);
                return usuarioRepository.actualizar(usuario);
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar usuario: " + e.getMessage(), e);
        }
    }

    @Transactional
    public boolean cambiarPassword(Long usuarioId, String passwordActual, String passwordNueva) {
        try {
            Usuario usuario = buscarPorId(usuarioId);
            if (usuario == null) {
                return false;
            }

            // Verificar contraseña actual
            String passwordActualEncriptada = encriptarPassword(passwordActual);
            if (!passwordActualEncriptada.equals(usuario.getPassword())) {
                return false;
            }

            // Validar nueva contraseña
            if (!validarPasswordSegura(passwordNueva)) {
                throw new RuntimeException("La nueva contraseña no cumple con los requisitos de seguridad");
            }

            // Actualizar contraseña
            String passwordNuevaEncriptada = encriptarPassword(passwordNueva);
            return usuarioRepository.actualizarPassword(usuarioId, passwordNuevaEncriptada);

        } catch (SQLException e) {
            throw new RuntimeException("Error al cambiar contraseña: " + e.getMessage(), e);
        }
    }

    public long contarUsuarios() {
        try {
            return usuarioRepository.contarUsuarios();
        } catch (SQLException e) {
            return 0;
        }
    }

    public long contarUsuariosActivos() {
        try {
            return usuarioRepository.contarUsuariosActivos();
        } catch (SQLException e) {
            return 0;
        }
    }

    // Metodo para encriptar contraseñas usando SHA-256
    private String encriptarPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder(2 * encodedHash.length);
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al encriptar contraseña", e);
        }
    }

    // Metodo para validar fortaleza de contraseña
    public boolean validarPasswordSegura(String password) {
        if (password == null || password.length() < 6) {
            // mensaje de error si la contraseña es nula o demasiado corta
            System.err.println("validarPasswordSegura error");
            return false;
        }

        boolean tieneMayuscula = password.matches(".*[A-Z].*");
        boolean tieneMinuscula = password.matches(".*[a-z].*");
        boolean tieneDigito = password.matches(".*\\d.*");

        return tieneMayuscula && tieneMinuscula && tieneDigito;
    }

    // Metodo para crear usuarios de demostración
    @Transactional
    public void crearUsuariosDemoSiNoExisten() {
        try {
            // Usuario administrador demo
            if (!existeEmail("admin@talentflow.com")) {
                Usuario admin = new Usuario();
                admin.setNombre("Admin");
                admin.setApellidos("TalentFlow");
                admin.setEmail("admin@talentflow.com");
                admin.setPassword("Admin123");
                admin.setEmpresa("TalentFlow HR");
                admin.setRol(Usuario.RolUsuario.ADMIN);
                admin.setActivo(true);
                crearUsuario(admin);
            }

            // Usuario regular demo
            if (!existeEmail("user@talentflow.com")) {
                Usuario user = new Usuario();
                user.setNombre("Usuario");
                user.setApellidos("Demo");
                user.setEmail("user@talentflow.com");
                user.setPassword("User123");
                user.setEmpresa("Empresa Demo");
                user.setRol(Usuario.RolUsuario.USER);
                user.setActivo(true);
                crearUsuario(user);
            }

        } catch (Exception e) {
            // Log del error pero no fallar la aplicación
            System.err.println("Error al crear usuarios demo: " + e.getMessage());
        }
    }

    // Metodo para verificar si un usuario es administrador
    public boolean esAdministrador(Usuario usuario) {
        return usuario != null &&
                usuario.getRol() != null &&
                Usuario.RolUsuario.ADMIN.equals(usuario.getRol());
    }

    // Metodo para verificar si un usuario puede acceder a una funcionalidad
    public boolean puedeAcceder(Usuario usuario, String funcionalidad) {
        if (usuario == null || !usuario.getActivo()) {
            return false;
        }

        // Los administradores pueden acceder a todo
        if (esAdministrador(usuario)) {
            return true;
        }

        // Definir permisos según la funcionalidad
        switch (funcionalidad.toLowerCase()) {
            case "dashboard":
            case "perfil":
            case "vacantes.ver":
                return true;
            case "usuarios.gestionar":
            case "vacantes.crear":
            case "vacantes.editar":
            case "vacantes.eliminar":
                return Usuario.RolUsuario.MANAGER.equals(usuario.getRol()) ||
                        Usuario.RolUsuario.ADMIN.equals(usuario.getRol());
            default:
                return false;
        }
    }
}