package org.puig.api.persistence.repository.operation;

import org.puig.api.persistence.entity.operation.Empleado;
import org.puig.api.persistence.repository.PuigRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmpleadoRepository extends PuigRepository<Empleado> {

    @Query("{$and: [{nickname: ?0}, {_class: ?1}]}")
    Optional<Empleado> findByNickname(String nickName, String _class);
}
