package org.puig.puigapi.service;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Optional;

@Getter
public abstract class PersistenceService <T, ID> {
    protected final MongoRepository<T, ID> repository;


    public PersistenceService(MongoRepository<T, ID> repository) {
        this.repository = repository;
    }

    public T save(@NotNull T t){
        return repository.save(t);
    }

    public List<T> save(@NotNull List<T> ts) {
        return ts.stream().map(this::save).toList();
    }


    public Optional<T> readByID(@NotNull ID id) {
        return  repository.findById(id);
    }

    public List<T> readAll(){
        return repository.findAll();
    }

    public boolean update(@NotNull T t){
        if(repository.exists(Example.of(t))) return false;
        repository.save(t);
        return true;
    }

    public boolean delete(@NotNull ID id){
        repository.deleteById(id);
        return repository.findById(id).isEmpty();
    }

    public boolean ifExists(@NotNull T t){
        try {
            return repository.exists(Example.of(t));
        } catch (EmptyStackException ex){
            System.out.println( ex);
            return false;
        }
    }

    public Optional<T> findBy(T t){return repository.findOne(Example.of(t));}


}

