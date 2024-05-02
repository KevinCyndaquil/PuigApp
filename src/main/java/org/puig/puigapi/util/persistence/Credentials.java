package org.puig.puigapi.util.persistence;

import jakarta.validation.constraints.NotBlank;
import org.jetbrains.annotations.NotNull;

/**
 * Representa un identificador de logeo.
 * @param identifier el identificador del usuario.
 * @param password su contraseña.
 */
public record Credentials (
        @NotBlank(message = "Agrega un identificador a las credenciales")
        String identifier,
        @NotBlank(message = "Agregar una contraseña a las credenciales")
        String password) {
}