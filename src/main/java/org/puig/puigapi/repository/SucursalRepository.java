package org.puig.puigapi.repository;

import org.puig.puigapi.persistence.entity.operation.Sucursal;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SucursalRepository extends MongoRepository<Sucursal, String> {
}
