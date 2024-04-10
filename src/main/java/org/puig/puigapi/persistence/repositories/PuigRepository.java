package org.puig.puigapi.persistence.repositories;

import org.puig.puigapi.persistence.entity.utils.persistence.Irrepetibe;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface PuigRepository<T extends Irrepetibe<ID>, ID>
        extends MongoRepository<T, ID> {
    @Query("{'_class': ?0}")
    List<T> findAllByClass(String classCanonicalName);
}
