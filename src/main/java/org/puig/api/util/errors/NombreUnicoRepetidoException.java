package org.puig.api.util.errors;

import lombok.Getter;
import lombok.NonNull;

@Getter
public class NombreUnicoRepetidoException extends RuntimeException {
    private final Object name;

    public <N> NombreUnicoRepetidoException(@NonNull Class<?> clazz, N name) {
        super("Nombre de %s declarado como unico ya existe usando el valor %s"
                .formatted(clazz.getSimpleName(), name));
        this.name = name;
    }
}
