package org.puig.puigapi.persistence.repositories;

import org.puig.puigapi.persistence.entity.operation.Empleado;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonaRepository extends MongoRepository<Empleado, String> {
    Empleado findByUsername(String username);
}
