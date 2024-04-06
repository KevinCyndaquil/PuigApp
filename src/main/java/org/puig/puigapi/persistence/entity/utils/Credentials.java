package org.puig.puigapi.persistence.entity.utils;

/**
 * Representa un identificador de logeo.
 * @param identifier el identificador del usuario.
 * @param password su contraseña.
 */
public record Credentials <ID> (ID identifier,
                                String password) {
}
