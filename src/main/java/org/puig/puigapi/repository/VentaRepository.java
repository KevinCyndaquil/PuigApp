package org.puig.puigapi.repository;

import org.puig.puigapi.persistence.entity.finances.Venta;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VentaRepository extends MongoRepository<Venta, String> {
}
