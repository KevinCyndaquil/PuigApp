package org.puig.puigapi.service;

import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.errors.BusquedaSinResultadoException;
import org.puig.puigapi.errors.LlaveDuplicadaException;
import org.puig.puigapi.persistence.entity.utils.persistence.Irrepetibe;
import org.puig.puigapi.persistence.repositories.PuigRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

import java.util.List;

public abstract class PersistenceService <T extends Irrepetibe<ID>, ID> {
    protected final PuigRepository<T, ID> repository;
    protected final Class<T> clazz;
    protected final String className;

    public PersistenceService(PuigRepository<T, ID> repository,
                              @NotNull Class<T> clazz) {
        this.repository = repository;
        this.clazz = clazz;
        this.className = clazz.getName();
    }

    public T save(@NotNull T t) throws LlaveDuplicadaException {
        if (t.getId() != null)
            if (repository.existsById(t.getId()))
                throw new LlaveDuplicadaException(t.getClass(), t.getId());

        return repository.insert(t);
    }

    public List<T> save(@NotNull List<T> ts) throws LlaveDuplicadaException {
        return ts.stream().map(this::save).toList();
    }

    public T readByID(@NotNull ID id) throws BusquedaSinResultadoException {
        return repository.findById(id)
                .orElseThrow(() -> new BusquedaSinResultadoException("id", id));
    }

    public List<T> readAll() {
        return repository.findAllByClass(className);
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
}

