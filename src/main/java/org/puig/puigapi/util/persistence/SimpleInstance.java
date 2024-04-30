package org.puig.puigapi.util.persistence;


import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import org.jetbrains.annotations.Contract;

/**
 * Representa una referencia o una instancia simple con un ID.
 * @param id el ID referenciado.
 * @param <ID> el tipo del ID.
 */

public record SimpleInstance <ID> (
        @NotNull(message = "Es obligatorio proporcionar un id para una referencia")
        ID id) {

    public <E extends Irrepetibe<ID>> @NonNull E instance(@NonNull Class<E> _class) {
        try {
            var c = _class.getConstructor().newInstance();
            c.setId(id);

            return c;
        } catch (ReflectiveOperationException e) {
            throw new InstancedException(e);
        }
    }

    @Contract("_ -> new")
    public static <ID> @NonNull SimpleInstance<ID> of(ID id) {
        return new SimpleInstance<>(id);
    }

    public static class InstancedException extends RuntimeException {
        public InstancedException(Throwable cause) {
            super(cause);
        }
    }
}
