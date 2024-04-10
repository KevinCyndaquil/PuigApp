package org.puig.puigapi.persistence.entity.utils.persistence;

import jakarta.validation.constraints.NotBlank;
import org.jetbrains.annotations.NotNull;

/**
 * Representa un identificador de logeo.
 * @param identifier el identificador del usuario.
 * @param password su contraseña.
 */
public record Credentials <ID> (@NotNull ID identifier,
                                @NotBlank(message = "Agregar una contraseña a las credenciales")
                                String password) {
}