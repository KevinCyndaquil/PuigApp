package org.puig.puigapi.service.admin;

import org.puig.puigapi.persistence.entity.admin.Proveedor;
import org.puig.puigapi.persistence.repositories.admin.ProveedorProductoRepository;
import org.puig.puigapi.service.PersistenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductoProveedorService
        extends PersistenceService<Proveedor.Producto, String> {

    @Autowired
    public ProductoProveedorService(ProveedorProductoRepository repository) {
        super(repository, Proveedor.Producto.class);
    }
}
