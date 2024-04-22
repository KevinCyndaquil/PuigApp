package org.puig.puigapi.controller.inside.operation;

import org.puig.puigapi.controller.PersistenceController;
import org.puig.puigapi.persistence.entity.operation.Sucursal;
import org.puig.puigapi.service.operation.ProductoSucursalService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sucursales/productos")
public class ProductoSucursalController
        extends PersistenceController<Sucursal.Producto, String, Sucursal.Producto.Request> {
    public ProductoSucursalController(ProductoSucursalService service) {
        super(service);
    }
}
