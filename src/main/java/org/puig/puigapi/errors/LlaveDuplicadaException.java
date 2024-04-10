package org.puig.puigapi.errors;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public class LlaveDuplicadaException extends RuntimeException {
    public Object id;

    public <ID> LlaveDuplicadaException(@NotNull Class<?> clazz, ID id) {
        super("Llave duplicada al intentar insertar %s usando el id %s"
                .formatted(clazz.getSimpleName(), id));
        this.id = id;
    }
}
