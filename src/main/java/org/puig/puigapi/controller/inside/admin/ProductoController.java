package org.puig.puigapi.controller.inside.admin;

import org.puig.puigapi.controller.PersistenceController;
import org.puig.puigapi.persistence.entity.admin.ProductoTienda;
import org.puig.puigapi.service.admin.ProductoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/productos")
public class ProductoController extends PersistenceController<ProductoTienda, String> {
    public ProductoController(ProductoService service) {
        super(service);
    }
}
