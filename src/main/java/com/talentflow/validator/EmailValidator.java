package com.talentflow.validator;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;
import java.util.regex.Pattern;

/**
 * JSF Custom Validator para validación de emails
 * Implementa: JSF Validator, Expression Language, Faces Messages
 */
@FacesValidator("emailValidator")
public class EmailValidator implements Validator<String> {

    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    @Override
    public void validate(FacesContext context, UIComponent component, String value)
            throws ValidatorException {

        if (value == null || value.trim().isEmpty()) {
            return; // Dejar que required validator maneje esto
        }

        String email = value.trim();

        if (!pattern.matcher(email).matches()) {
            String message = "El formato del email no es válido";

            // Intentar obtener mensaje del resource bundle
            try {
                message = context.getApplication()
                        .getResourceBundle(context, "i18n.messages")
                        .getString("form.email.invalid");
            } catch (Exception e) {
                // Usar mensaje por defecto si no se encuentra en resource bundle
            }

            throw new ValidatorException(
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de Validación", message));
        }
    }
}