package org.puig.puigapi.persistence.repositories.finances;

import org.puig.puigapi.persistence.entity.finances.Venta;
import org.puig.puigapi.persistence.repositories.PuigRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VentaRepository
        extends PuigRepository<Venta, String> {
}