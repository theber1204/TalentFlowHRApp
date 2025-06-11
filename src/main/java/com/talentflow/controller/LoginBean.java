package com.talentflow.controller;

import com.talentflow.model.Usuario;
import com.talentflow.service.UsuarioService;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.primefaces.PrimeFaces;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Named
@SessionScoped
public class LoginBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
    private boolean recordarme;

    @Inject
    private UsuarioService usuarioService;

    // Constructor vacío necesario
    public LoginBean() {
        this.username = "";
        this.password = "";
        this.recordarme = false;
    }

    public String loginModalSpecial() {
        try {
            System.out.println("Intento de login con usuario: " + username);

            Usuario usuario = usuarioService.autenticar(username, password);

            if (usuario != null) {
                // Login exitoso
                System.out.println("Login exitoso para " + username);

                // Guardar usuario en sesión
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
                        .put("usuario", usuario.getNombreCompleto());
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
                        .put("usuarioObj", usuario);

                // Usar la URL amigable
                String dashboardUrl = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/dashboard";

                // Parámetros para la redirección en javascript
                PrimeFaces.current().ajax().addCallbackParam("loggedIn", true);
                PrimeFaces.current().ajax().addCallbackParam("dashboard", dashboardUrl);

                return null;
            } else {
                // Login fallido
                System.out.println("Login fallido para " + username);
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "Error", "Usuario o contraseña incorrectos"));
                PrimeFaces.current().ajax().addCallbackParam("loggedIn", false);
                return null;
            }
        } catch (Exception e) {
            System.out.println("Error en loginModalSpecial: " + e.getMessage());
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Error", "Ha ocurrido un error inesperado"));
            PrimeFaces.current().ajax().addCallbackParam("loggedIn", false);
            return null;
        }
    }


    public void mostrarMensajeRecuperacion() {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Recuperación de contraseña",
                        "Se ha enviado un correo con instrucciones"));
    }

    public String cerrarSesion() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/login?faces-redirect=true";
    }

    // Getters y setters completos
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isRecordarme() {
        return recordarme;
    }

    public void setRecordarme(boolean recordarme) {
        this.recordarme = recordarme;
    }
}