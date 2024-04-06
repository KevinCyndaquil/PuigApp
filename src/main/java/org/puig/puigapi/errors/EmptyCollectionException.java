package org.puig.puigapi.errors;

import org.jetbrains.annotations.NotNull;

public class EmptyCollectionException extends IllegalArgumentException {
    public EmptyCollectionException(@NotNull Class<?> clazz, String collectionName) {
        super("Collection %s in entity %s must have at least one item"
                .formatted(collectionName, clazz.getSimpleName()));
    }
}
