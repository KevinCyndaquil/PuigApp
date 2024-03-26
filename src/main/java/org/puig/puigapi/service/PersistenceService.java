package org.puig.puigapi.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public abstract class PersistenceService <T, ID> {
    protected final MongoRepository<T, ID> repository;

    public PersistenceService(MongoRepository<T, ID> repository) {
        this.repository = repository;
    }

    public @Nullable T save(@NotNull T t){
        if (exists(t)) return null;
        return repository.save(t);
    }

    public List<T> save(@NotNull List<T> ts) {
        return ts.stream().map(this::save).toList();
    }

    public Optional<T> readByID(@NotNull ID id) {
        return  repository.findById(id);
    }

    public List<T> read(@NotNull T t) {
        return repository.findAll(Example.of(t));
    }

    public List<T> readAll(){
        return repository.findAll();
    }

    public boolean update(@NotNull T t) {
        if(exists(t)) return false;
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

