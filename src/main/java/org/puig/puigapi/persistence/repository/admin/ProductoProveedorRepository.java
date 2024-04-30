package org.puig.puigapi.persistence.repository.admin;

import org.bson.types.ObjectId;
import org.puig.puigapi.persistence.entity.admin.Proveedor;
import org.puig.puigapi.persistence.repository.PuigRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoProveedorRepository
        extends PuigRepository<Proveedor.Producto, ObjectId> {
}
