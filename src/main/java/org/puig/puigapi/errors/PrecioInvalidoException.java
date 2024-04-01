package org.puig.puigapi.errors;

import org.jetbrains.annotations.NotNull;

/**
 * Cuando un precio o monto es invalido.
 */
public class PrecioInvalidoException extends IllegalArgumentException {
    public PrecioInvalidoException(@NotNull Class<?> classError) {
        super("Precio o monto de %s debe ser no nulo o mayor a zero"
                .formatted(classError.getName()));
    }
}
