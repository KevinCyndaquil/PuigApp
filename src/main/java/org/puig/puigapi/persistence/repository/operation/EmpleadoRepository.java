package org.puig.puigapi.persistence.repository.operation;

import org.bson.types.ObjectId;
import org.puig.puigapi.persistence.entity.operation.Empleado;
import org.puig.puigapi.persistence.repository.PuigRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmpleadoRepository
        extends PuigRepository<Empleado, ObjectId> {

    @Query("{$and: [{nickname: ?0}, {_class: ?1}]}")
    Optional<Empleado> findByNickname(String nickName, String _class);
}
