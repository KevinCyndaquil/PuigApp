package org.puig.puigapi.service.admin;

import org.puig.puigapi.persistence.entity.admin.Proveedor;
import org.puig.puigapi.persistence.repositories.admin.ProductoProveedorRepository;
import org.puig.puigapi.service.PersistenceService;
import org.puig.puigapi.service.annotations.PuigService;
import org.springframework.beans.factory.annotation.Autowired;

@PuigService(Proveedor.Producto.class)
public class ProductoProveedorService extends
        PersistenceService<Proveedor.Producto, String, ProductoProveedorRepository> {

    public ProductoProveedorService(ProductoProveedorRepository repository) {
        super(repository);
    }
}
