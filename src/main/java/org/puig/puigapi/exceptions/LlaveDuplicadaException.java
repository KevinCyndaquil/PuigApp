package org.puig.puigapi.exceptions;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

@Getter
public class LlaveDuplicadaException extends RuntimeException {
    public Object id;
    public String idAsString;

    public <ID> LlaveDuplicadaException(@NotNull Class<?> clazz, ID id) {
        super("Llave duplicada al intentar insertar %s usando el id %s"
                .formatted(clazz.getSimpleName(), id));
        this.id = id;
        this.idAsString = id.toString();
    }

    @SafeVarargs
    public <ID> LlaveDuplicadaException(@NotNull Class<?> clazz, ID... id) {
        super("Llave duplicada al intentar insertar %s usando los id's %s"
                .formatted(clazz.getSimpleName(), Arrays.toString(id)));
        this.id = id;
        this.idAsString = Arrays.toString(id);
    }

    public String showId() {
        return idAsString;
    }
}
