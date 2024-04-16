package org.puig.puigapi.service.operation;

import org.puig.puigapi.persistence.entity.operation.Sucursal;
import org.puig.puigapi.persistence.repositories.operation.SucursalRepository;
import org.puig.puigapi.service.PersistenceService;
import org.puig.puigapi.service.annotations.PuigService;

@PuigService(Sucursal.class)
public class SucursalService extends
        PersistenceService<Sucursal, String, SucursalRepository> {

    public SucursalService(SucursalRepository repository) {
        super(repository);
    }
}
