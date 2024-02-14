package org.puig.puigapi.repository;

import org.puig.puigapi.persistence.entity.utils.Tarjeta;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TarjetaRepository extends MongoRepository<Tarjeta, String> {
}
