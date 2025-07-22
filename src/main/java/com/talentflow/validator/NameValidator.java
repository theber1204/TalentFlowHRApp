package com.talentflow.validator;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;
import java.util.regex.Pattern;

@FacesValidator("nameValidator")
public class NameValidator implements Validator<String> {

    // Acepta letras, espacios, acentos y algunos caracteres especiales comunes en
    // nombres
    private static final String NAME_PATTERN = "^[\\p{L}\\s.'\\-]{2,50}$";
    private static final Pattern pattern = Pattern.compile(NAME_PATTERN);

    @Override
    public void validate(FacesContext context, UIComponent component, String value)
            throws ValidatorException {

        if (value == null || value.trim().isEmpty()) {
            return; // Dejar que required validator maneje esto
        }

        String name = value.trim();

        // Validar longitud
        if (name.length() < 2 || name.length() > 50) {
            throw new ValidatorException(
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Error de Validación",
                            "El nombre debe tener entre 2 y 50 caracteres"));
        }

        // Validar caracteres permitidos
        if (!pattern.matcher(name).matches()) {
            String message = "El nombre contiene caracteres no válidos";

            // Intentar obtener mensaje del resource bundle
            try {
                message = context.getApplication()
                        .getResourceBundle(context, "i18n.messages")
                        .getString("form.name.invalid");
            } catch (Exception e) {
                // Usar mensaje por defecto si no se encuentra en resource bundle
            }

            throw new ValidatorException(
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de Validación", message));
        }

        // Validar que no haya espacios múltiples
        if (name.contains("  ")) {
            throw new ValidatorException(
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Error de Validación",
                            "El nombre no puede contener espacios múltiples"));
        }
    }
}