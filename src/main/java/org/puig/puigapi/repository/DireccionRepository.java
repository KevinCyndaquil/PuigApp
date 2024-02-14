package org.puig.puigapi.repository;

import org.puig.puigapi.persistence.entity.utils.Direccion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DireccionRepository extends MongoRepository<Direccion, String > {
}
