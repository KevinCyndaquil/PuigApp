package org.puig.puigapi.service.operation;

import org.puig.puigapi.persistence.entity.operation.Sucursal;
import org.puig.puigapi.persistence.repositories.operation.SucursalRepository;
import org.puig.puigapi.service.PersistenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SucursalService extends PersistenceService<Sucursal, String> {
    @Autowired
    public SucursalService(SucursalRepository repository) {
        super(repository, Sucursal.class);
    }

    @Override
    public List<Sucursal> readAll() {
        return repository.findAllByClass(Sucursal.class.getCanonicalName());
    }
}
