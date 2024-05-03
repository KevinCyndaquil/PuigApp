package org.puig.puigapi.service.admin;

import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.persistence.entity.admin.Proveedor;
import org.puig.puigapi.persistence.repository.admin.ProductoProveedorRepository;
import org.puig.puigapi.service.PersistenceService;
import org.puig.puigapi.util.annotation.PuigService;
import org.puig.puigapi.util.persistence.SimpleInstance;

import java.util.Set;

@PuigService(Proveedor.Producto.class)
public class ProductoProveedorService extends
        PersistenceService<Proveedor.Producto, ObjectId, ProductoProveedorRepository> {

    public ProductoProveedorService(ProductoProveedorRepository repository) {
        super(repository);
    }

    public Set<Proveedor.Producto> readByProveedor(@NotNull SimpleInstance<ObjectId> proveedorInstance) {
        return repository.findByProveedorId(proveedorInstance.id());
    }
}
