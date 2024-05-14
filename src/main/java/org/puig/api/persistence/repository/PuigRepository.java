package org.puig.api.persistence.repository;

import lombok.NonNull;
import org.bson.types.ObjectId;
import org.puig.api.util.persistence.Unico;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface PuigRepository<T extends Unico>
        extends MongoRepository<T, ObjectId> {

    @Query("{_class: ?0}")
    @NonNull List<T> findByClass(@NonNull String _class);

    @Query("{$or: ?0}")
    @NonNull List<T> findByClasses(@NonNull Object[] matches);

    @Query("{_id: ?0, _class: ?1}")
    Optional<T> findById(@NonNull ObjectId _id, @NonNull String _class);

    @Query("{$and: [{_id: ?0}, {$or: ?1}]}")
    Optional<T> findById(@NonNull ObjectId _id, @NonNull Object... _classes);

    @Query("{$and: [{nombre: ?0}, {_class: ?1}]}")
    <N> Optional<T> findByNombre(@NonNull N name, @NonNull String _class);
}
