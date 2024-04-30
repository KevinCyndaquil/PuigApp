package org.puig.puigapi.service;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.exceptions.BusquedaSinResultadoException;
import org.puig.puigapi.exceptions.LlaveDuplicadaException;
import org.puig.puigapi.exceptions.NombreUnicoRepetidoException;
import org.puig.puigapi.util.persistence.Irrepetibe;
import org.puig.puigapi.persistence.repository.PuigRepository;
import org.puig.puigapi.util.persistence.SimpleInstance;
import org.puig.puigapi.util.persistence.UniqueName;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class PersistenceService <
        T extends Irrepetibe<ID>,
        ID,
        R extends PuigRepository<T, ID>> {

    protected final R repository;

    @Setter @Getter protected Class<?> type;

    public T save(@NotNull T t) throws LlaveDuplicadaException {
        if (t.getId() != null)
            if (repository.existsById(t.getId()))
                throw new LlaveDuplicadaException(t.getClass(), t.getId());
        if (t instanceof UniqueName<?> unique)
            if (repository.findByNombre(unique.getNombre(), type.getName()).isPresent())
                throw new NombreUnicoRepetidoException(t.getClass(), unique.getNombre());

        return repository.insert(t);
    }

    public List<T> save(@NotNull List<T> ts) throws LlaveDuplicadaException {
        return ts.stream().map(this::save).toList();
    }

    public T readById(@NotNull ID id) throws BusquedaSinResultadoException {
        return repository.findById(id, type.getName())
                .orElseThrow(() -> new BusquedaSinResultadoException("id", id));
    }

    public T readById(@NotNull SimpleInstance<ID> simpleInstance)
            throws BusquedaSinResultadoException {
        return readById(simpleInstance.id());
    }

    public <E extends Irrepetibe<ID>> T readById(@NotNull E e) throws BusquedaSinResultadoException {
        if (Objects.isNull(e.getId()))
            throw new IllegalArgumentException("Objeto proporcionado durante la busqueda contiene un id nulo");
        return readById(e.getId());
    }

    /**
     * Busca en una colección y retorna solo aquellas entidades que sean del mismo tipo que la de este
     * servicio.
     * @return una lista con las entidades del mismo tipo que este.
     */
    public List<T> readAllWhile() {
        return repository.findByClass(type.getName());
    }

    public List<T> readAllWhile(@NotNull Class<?>... _classes) {
        Object[] params = Arrays.stream(_classes)
                .map(c -> new Document("_class", c.getName()))
                .toArray();
        return repository.findByClasses(params);
    }

    /**
     * Realiza una busqueda or.
     * @param t los atributos a considerar en la busqueda.
     * @return los atributos que coíncidan con la busqueda.
     */
    public List<T> read(@NotNull T t) {
        ExampleMatcher matcher = ExampleMatcher.matchingAny()
                .withIgnorePaths("_id")
                .withIgnoreCase()
                .withIgnoreNullValues()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<T> example = Example.of(t, matcher);

        return repository.findAll(example);
    }

    public boolean update(@NotNull T t) {
        if(!repository.existsById(t.getId())) return false;
        repository.save(t);
        return true;
    }

    public boolean delete(@NotNull ID id) {
        repository.deleteById(id);
        return repository.findById(id).isEmpty();
    }

    public int count(@NotNull Class<?>... _classes) {
        return readAllWhile(_classes).size();
    }
}

