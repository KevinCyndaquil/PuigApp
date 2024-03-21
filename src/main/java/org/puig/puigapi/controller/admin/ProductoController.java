package org.puig.puigapi.controller.admin;

import org.puig.puigapi.controller.PersistenceController;
import org.puig.puigapi.persistence.entity.admin.Producto;
import org.puig.puigapi.service.admin.ProductoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/producto")
public class ProductoController extends PersistenceController<Producto, String> {
    public ProductoController(ProductoService service) {
        super(service);
    }
}
