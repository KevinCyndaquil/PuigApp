package org.puig.puigapi.exceptions;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
public class NombreUnicoRepetidoException extends RuntimeException {
    private Object name;

    public <N> NombreUnicoRepetidoException(@NotNull Class<?> clazz, N name) {
        super("Nombre de %s declarado como unico ya existe usando el valor %s"
                .formatted(clazz.getSimpleName(), name));
        this.name = name;
    }
}
