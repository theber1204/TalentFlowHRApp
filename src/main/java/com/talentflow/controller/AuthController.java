package com.talentflow.controller;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.Map;

@Named
@SessionScoped
public class AuthController implements Serializable {

    private static final long serialVersionUID = 1L;
    private String authMode = "login";

    public void setAuthMode() {
        Map<String,String> params = FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap();
        String mode = params.get("mode");

        if (mode != null && !mode.isEmpty()) {
            this.authMode = mode;
        }
    }

    public String getAuthMode() {
        return authMode;
    }
}