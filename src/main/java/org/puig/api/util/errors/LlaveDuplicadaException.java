package org.puig.api.util.errors;

import lombok.Getter;
import lombok.NonNull;

import java.util.Arrays;

@Getter
public class LlaveDuplicadaException extends RuntimeException {
    public Object id;
    public String idAsString;

    public <ID> LlaveDuplicadaException(@NonNull Class<?> clazz, ID id) {
        super("Llave duplicada al intentar insertar %s usando el id %s"
                .formatted(clazz.getSimpleName(), id));
        this.id = id;
        this.idAsString = id.toString();
    }

    @SafeVarargs
    public <ID> LlaveDuplicadaException(@NonNull Class<?> clazz, ID... id) {
        super("Llave duplicada al intentar insertar %s usando los id's %s"
                .formatted(clazz.getSimpleName(), Arrays.toString(id)));
        this.id = id;
        this.idAsString = Arrays.toString(id);
    }

    public String showId() {
        return idAsString;
    }
}
