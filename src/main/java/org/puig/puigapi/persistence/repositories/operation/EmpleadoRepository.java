package org.puig.puigapi.persistence.repositories.operation;

import org.puig.puigapi.persistence.entity.operation.Empleado;
import org.puig.puigapi.persistence.repositories.PuigRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmpleadoRepository
        extends PuigRepository<Empleado, String> {
    Optional<Empleado> findByNickname(String nickName);
}
