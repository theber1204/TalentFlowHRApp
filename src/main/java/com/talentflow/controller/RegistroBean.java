package com.talentflow.controller;

import com.talentflow.model.Usuario;
import com.talentflow.service.UsuarioService;

import jakarta.faces.application.FacesMessage;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import org.primefaces.PrimeFaces;
import java.io.Serializable;

@Named("registroBean")
@RequestScoped
public class RegistroBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Usuario usuario;
    private String confirmPassword;
    private boolean aceptaTerminos;

    @Inject
    private UsuarioService usuarioService;

    public RegistroBean() {
        usuario = new Usuario();
    }

    /**
     * Metodo para registro en modal
     */
    public String registrarModal() {
        try {
            System.out.println("Iniciando registro: " + usuario.getEmail());

            // Validaciones comunes
            if (!validarDatos()) {
                System.out.println("Falló validación de datos para: " + usuario.getEmail());
                PrimeFaces.current().ajax().addCallbackParam("validationFailed", true);
                return null;
            }

            // Crear el usuario
            System.out.println("Intentando crear usuario: " + usuario.getEmail());
            Usuario nuevoUsuario = usuarioService.crearUsuario(usuario);

            if (nuevoUsuario != null) {
                System.out.println("Usuario creado exitosamente: " + nuevoUsuario.getId());
                PrimeFaces.current().ajax().addCallbackParam("registrado", true);
                PrimeFaces.current().ajax().addCallbackParam("dashboard", "/pages/index.xhtml");
                return null;
            } else {
                System.out.println("Error: nuevoUsuario es null");
                PrimeFaces.current().ajax().addCallbackParam("registrado", false);
                return null;
            }
        } catch (Exception e) {
            System.err.println("Excepción en registrarModal: " + e.getMessage());
            e.printStackTrace();
            PrimeFaces.current().ajax().addCallbackParam("registrado", false);
            PrimeFaces.current().ajax().addCallbackParam("error", e.getMessage());
            return null;
        }
    }

    /**
     * Metodo para registro en página completa
     */
    public String registrar() {
        try {
            // Validaciones comunes
            if (!validarDatos()) {
                System.out.println("Errores en validación de datos");
                return null;
            }

            // Crear el usuario
            Usuario usuarioCreado = usuarioService.crearUsuario(usuario);

            if (usuarioCreado != null) {
                FacesContext.getCurrentInstance().getExternalContext()
                        .getFlash().put("usuarioRegistrado", usuario.getNombre());

                // Limpiar el formulario
                limpiarFormulario();

                return "success";
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "Error", "Error al registrar el usuario"));
                return null;
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Error", "Error interno del servidor: " + e.getMessage()));
            return null;
        }
    }

    private boolean validarDatos() {
        System.out.println("Validando datos para: " + usuario.getEmail());
        boolean esValido = true;

        // Validar que las contraseñas coincidan
        if (confirmPassword == null || !usuario.getPassword().equals(confirmPassword)) {
            System.out.println("Error: Las contraseñas no coinciden");
            esValido = false;
        }

        // Validar términos y condiciones
        if (!aceptaTerminos) {
            System.out.println("Error: No aceptó términos y condiciones");
            esValido = false;
        }

        // Validar fortaleza de contraseña
        if (!usuarioService.validarPasswordSegura(usuario.getPassword())) {
            System.out.println("Error: Contraseña no cumple requisitos de seguridad");
            esValido = false;
        }

        // Verificar si el email ya existe
        if (usuarioService.existeEmail(usuario.getEmail())) {
            System.out.println("Error: El email ya está registrado");
            esValido = false;
        }

        return esValido;
    }

    /**
     * Limpia el formulario después del registro
     */
    private void limpiarFormulario() {
        usuario = new Usuario();
        confirmPassword = "";
        aceptaTerminos = false;
    }

    public String volverAlLogin() {
        return "/pages/login?faces-redirect=true";
    }

    public String volverAlInicio() {
        return "/index?faces-redirect=true";
    }

    // Getters y Setters
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public boolean isAceptaTerminos() {
        return aceptaTerminos;
    }

    public void setAceptaTerminos(boolean aceptaTerminos) {
        this.aceptaTerminos = aceptaTerminos;
    }
}