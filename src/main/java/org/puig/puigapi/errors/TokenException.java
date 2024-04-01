package org.puig.puigapi.errors;

import java.util.Date;

public class TokenException extends RuntimeException {

    public TokenException(String message, String hint) {
        super("token_error");
    }

    public static class Expired extends TokenException {

        public Expired(Date createdAt) {
            super("El token proporcionado ya está caducado",
                    "Vuelve a loggearte para obtener uno nuevo");
        }
    }

    public static class Invalid extends TokenException {

        public Invalid(String tokenInvalido) {
            super("El token proporcionado '%s' es invalido para este servicio"
                            .formatted(tokenInvalido),
                    "Registrate o loggeate si ya tienes un usario para obtener un token válido, " +
                            "agregalo a la cabecera cómo un Bearer JWT");
        }
    }
}
