package org.puig.api.util.errors;

public class PasswordIncorrectoException extends RuntimeException {
    public PasswordIncorrectoException(String identifier) {
        super("Error durante el logeo del usuario %s, intenta usar una contraseña diferente"
                .formatted(identifier));
    }
}
