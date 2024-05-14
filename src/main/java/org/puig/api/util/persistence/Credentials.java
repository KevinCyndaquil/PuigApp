package org.puig.api.util.persistence;

import jakarta.validation.constraints.NotBlank;
import org.puig.api.util.grupos.SimpleInfo;

/**
 * Representa un identificador de logeo.
 * @param identifier el identificador del usuario.
 * @param password su contraseña.
 */
public record Credentials (
        @NotBlank(message = "Agrega un identificador a las credenciales",
                groups = SimpleInfo.class)
        String identifier,
        @NotBlank(message = "Agregar una contraseña a las credenciales",
                groups = SimpleInfo.class)
        String password) {
}