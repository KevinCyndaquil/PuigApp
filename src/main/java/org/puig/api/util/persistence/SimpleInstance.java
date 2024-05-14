package org.puig.api.util.persistence;


import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.puig.api.util.grupos.UniqueInfo;

/**
 * Representa una referencia o una instancia simple con un ID.
 * @param id el ID referenciado.
 */

public record SimpleInstance (
        @NotNull(message = "Es obligatorio proporcionar un id para una referencia", groups = UniqueInfo.class)
        ObjectId id) {

    public <E extends Unico> @NonNull E instance(@NonNull Class<E> _class) {
        try {
            var c = _class.getConstructor().newInstance();
            c.setId(id);

            return c;
        } catch (ReflectiveOperationException e) {
            throw new InstancedException(e);
        }
    }

    public static @NonNull SimpleInstance of(ObjectId id) {
        return new SimpleInstance(id);
    }

    public static class InstancedException extends RuntimeException {
        public InstancedException(Throwable cause) {
            super(cause);
        }
    }
}
