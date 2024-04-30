package org.puig.puigapi.persistence.repository.operation;

import org.puig.puigapi.persistence.entity.operation.Sucursal;
import org.puig.puigapi.persistence.repository.PuigRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SucursalRepository
        extends PuigRepository<Sucursal, String> {

}
