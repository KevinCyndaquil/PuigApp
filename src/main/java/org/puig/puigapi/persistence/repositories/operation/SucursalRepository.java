package org.puig.puigapi.persistence.repositories.operation;

import org.puig.puigapi.persistence.entity.operation.Sucursal;
import org.puig.puigapi.persistence.repositories.PuigRepository;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SucursalRepository
        extends PuigRepository<Sucursal, String> {
}
