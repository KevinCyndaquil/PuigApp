package org.puig.puigapi.controller.inside.admin;

import org.puig.puigapi.controller.PersistenceController;
import org.puig.puigapi.persistence.entity.admin.ProductoTienda;
import org.puig.puigapi.service.admin.ProductoTiendaService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/productos_tienda")
public class ProductoTiendaController
        extends PersistenceController<ProductoTienda, String, ProductoTienda.Post> {
    public ProductoTiendaController(ProductoTiendaService service) {
        super(service);
    }
}
