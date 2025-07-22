package com.talentflow.validator;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;
import java.util.regex.Pattern;

@FacesValidator("phoneValidator")
public class PhoneValidator implements Validator<String> {

    // Acepta formatos: +123456789, 123-456-789, (123) 456-789, etc.
    private static final String PHONE_PATTERN = "^\\+?([0-9]{1,3})?[-. ]?\\(?([0-9]{3})\\)?[-. ]?([0-9]{3})[-. ]?([0-9]{3,4})$";
    private static final Pattern pattern = Pattern.compile(PHONE_PATTERN);

    @Override
    public void validate(FacesContext context, UIComponent component, String value)
            throws ValidatorException {

        if (value == null || value.trim().isEmpty()) {
            return; // Dejar que required validator maneje esto
        }

        String phone = value.trim().replaceAll("\\s+", ""); // Eliminar espacios extras

        if (!pattern.matcher(phone).matches()) {
            String message = "El formato del teléfono no es válido";

            // Intentar obtener mensaje del resource bundle
            try {
                message = context.getApplication()
                        .getResourceBundle(context, "i18n.messages")
                        .getString("form.phone.invalid");
            } catch (Exception e) {
                // Usar mensaje por defecto si no se encuentra en resource bundle
            }

            throw new ValidatorException(
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de Validación", message));
        }
    }
}