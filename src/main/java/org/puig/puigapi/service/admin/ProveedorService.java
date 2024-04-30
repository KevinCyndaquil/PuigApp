package org.puig.puigapi.service.admin;

import org.bson.types.ObjectId;
import org.puig.puigapi.persistence.entity.admin.Proveedor;
import org.puig.puigapi.persistence.repository.admin.ProveedorRepository;
import org.puig.puigapi.service.PersistenceService;
import org.puig.puigapi.util.annotation.PuigService;

@PuigService(Proveedor.class)
public class ProveedorService extends
        PersistenceService<Proveedor, ObjectId, ProveedorRepository> {

    public ProveedorService(ProveedorRepository repository) {
        super(repository);
    }
}
