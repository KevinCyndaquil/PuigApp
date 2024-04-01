package org.puig.puigapi.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.puig.puigapi.errors.EntityException;
import org.puig.puigapi.persistence.entity.utils.Irrepetibe;
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

    public @Nullable T save(@NotNull T t) throws EntityException.NotSaved {
        if (t.getId() != null)
            if (repository.existsById(t.getId())) throw new EntityException.NotSaved(t);
        return repository.save(t);
    }

    public List<T> save(@NotNull List<T> ts) throws EntityException.NotSaved{
        return ts.stream().map(this::save).toList();
    }

    public T readByID(@NotNull ID id) throws EntityException.NotFind {
        return repository.findById(id)
                .orElseThrow(() -> new EntityException.NotFind(id));
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
                .withIgnorePaths("id")
                .withIgnoreCase()
                .withIgnoreNullValues()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<T> example = Example.of(t, matcher);

        return repository.findAll(example);
    }

    public boolean update(@NotNull T t) {
        if(!exists(t)) return false;
        repository.save(t);
        return true;
    }

    public boolean delete(@NotNull ID id) {
        repository.deleteById(id);
        return repository.findById(id).isEmpty();
    }

    public boolean exists(@NotNull T t) {
        return repository.exists(Example.of(t));
    }
}

