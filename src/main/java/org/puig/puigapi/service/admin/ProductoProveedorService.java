package org.puig.puigapi.service.admin;

import org.bson.types.ObjectId;
import org.puig.puigapi.persistence.entity.admin.Proveedor;
import org.puig.puigapi.persistence.repository.admin.ProductoProveedorRepository;
import org.puig.puigapi.service.PersistenceService;
import org.puig.puigapi.util.annotation.PuigService;

@PuigService(Proveedor.Producto.class)
public class ProductoProveedorService extends
        PersistenceService<Proveedor.Producto, ObjectId, ProductoProveedorRepository> {

    public ProductoProveedorService(ProductoProveedorRepository repository) {
        super(repository);
    }
}
