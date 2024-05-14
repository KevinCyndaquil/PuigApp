package org.puig.api.service;

import lombok.NonNull;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.puig.api.persistence.repository.PuigRepository;
import org.puig.api.util.errors.BusquedaSinResultadoException;
import org.puig.api.util.persistence.SimpleInstance;
import org.puig.api.util.persistence.Unico;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public interface ReadService <C extends Unico> {
    @NonNull PuigRepository<C> repository();
    @NonNull Class<C> clazz();

    default C readById(@NonNull ObjectId id) throws BusquedaSinResultadoException {
        return repository().findById(id, clazz().getName())
                .orElseThrow(() -> new BusquedaSinResultadoException("_id", id));
    }

    default C readById(@NonNull SimpleInstance instance) throws BusquedaSinResultadoException {
        return readById(instance.id());
    }

    default <E extends C> C readById(@NonNull E e) throws BusquedaSinResultadoException {
        if (!Objects.isNull(e.getId())) return readById(e.getId());
        throw new IllegalArgumentException("ID proporcionado durante la busqueda es nulo");
    }

    /**
     * Busca en una colección y retorna solo aquellas entidades que sean del mismo tipo que la de este
     * servicio.
     * @return una lista con las entidades del mismo tipo que este.
     */
    default List<C> readAllWhile() {
        return repository().findByClass(clazz().getName());
    }

    default List<C> readAllWhile(@NonNull Class<?>... _classes) {
        Object[] params = Arrays.stream(_classes)
                .map(c -> new Document("_class", c.getName()))
                .toArray();
        return repository().findByClasses(params);
    }

    /**
     * Realiza una busqueda or.
     * @param c los atributos a considerar en la busqueda.
     * @return los atributos que coíncidan con la busqueda.
     */
    default List<C> read(@NonNull C c) {
        ExampleMatcher matcher = ExampleMatcher.matchingAny()
                .withIgnorePaths("_id")
                .withIgnoreCase()
                .withIgnoreNullValues()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<C> example = Example.of(c, matcher);

        return repository().findAll(example);
    }
}
