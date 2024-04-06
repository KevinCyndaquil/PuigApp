package org.puig.puigapi.controller.inside.admin;

import org.puig.puigapi.controller.PersistenceController;
import org.puig.puigapi.persistence.entity.admin.Proveedor;
import org.puig.puigapi.service.admin.ProductoProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/proveedores/producto")
public class ProductoProveedorController
        extends PersistenceController<Proveedor.Producto, String, Proveedor.Producto.Post> {

    @Autowired
    protected ProductoProveedorController(ProductoProveedorService service) {
        super(service);
    }
}
