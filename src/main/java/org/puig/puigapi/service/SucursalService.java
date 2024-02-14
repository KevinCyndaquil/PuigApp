package org.puig.puigapi.service;

import org.puig.puigapi.persistence.entity.operation.Sucursal;
import org.puig.puigapi.repository.SucursalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SucursalService extends PersistenceService<Sucursal, String> {
    @Autowired
    public SucursalService(SucursalRepository repository){super(repository);}
}
