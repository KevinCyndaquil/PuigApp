package org.puig.api.service;

import lombok.*;
import org.bson.types.ObjectId;
import org.puig.api.util.persistence.Unico;

import java.util.Optional;

public interface PersistenceService <C extends Unico> extends SaveService<C>, ReadService<C> {

    default Optional<C> readByName(@NonNull String name) {
        return repository().findByNombre(name, clazz().getName());
    }

    default boolean update(@NonNull C c) {
        if(!repository().existsById(c.getId())) return false;
        repository().save(c);
        return true;
    }

    default boolean delete(@NonNull ObjectId id) {
        repository().deleteById(id);
        return repository().findById(id).isEmpty();
    }

    default int count(@NonNull Class<?>... _classes) {
        return readAllWhile(_classes).size();
    }
}

