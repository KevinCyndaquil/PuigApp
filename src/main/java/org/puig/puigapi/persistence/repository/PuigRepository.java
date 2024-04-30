package org.puig.puigapi.persistence.repository;

import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.util.persistence.Irrepetibe;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface PuigRepository<T extends Irrepetibe<ID>, ID>
        extends MongoRepository<T, ID> {

    @Query("{_class: ?0}")
    @NotNull List<T> findByClass(@NotNull String _class);

    @Query("{$or: ?0}")
    @NotNull List<T> findByClasses(@NotNull Object[] matches);

    @Query("{_id: ?0, _class: ?1}")
    Optional<T> findById(@NotNull ID _id, @NotNull String _class);

    @Query("{$and: [{_id: ?0}, {$or: ?1}]}")
    Optional<T> findById(@NotNull ID _id, @NotNull Object... _classes);

    @Query("{$and: [{nombre: ?0}, {_class: ?1}]}")
    <N> Optional<T> findByNombre(@NotNull N name, @NotNull String _class);
}
