package org.puig.puigapi.persistence.repositories.operation;

import org.puig.puigapi.persistence.entity.operation.Empleado;
import org.puig.puigapi.persistence.repositories.PuigRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmpleadoRepository
        extends PuigRepository<Empleado, String> {
    @Query("{$and: [{'_id': ?0}, {'_class': 'org.puig.puigapi.persistence.entity.operation.Empleado'}]}")

    Optional<Empleado> findByNickname(String nickName);
}
