package org.puig.puigapi.service.admin;

import org.puig.puigapi.persistence.entity.admin.Proveedor;
import org.puig.puigapi.persistence.repositories.admin.ProveedorRepository;
import org.puig.puigapi.service.PersistenceService;
import org.puig.puigapi.service.annotations.PuigService;

@PuigService(Proveedor.class)
public class ProveedorService extends
        PersistenceService<Proveedor, String, ProveedorRepository> {

    public ProveedorService(ProveedorRepository repository) {
        super(repository);
    }
}
