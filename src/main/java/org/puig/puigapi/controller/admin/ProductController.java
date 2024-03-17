package org.puig.puigapi.controller.admin;

import org.puig.puigapi.controller.PersistenceController;
import org.puig.puigapi.persistence.entity.admin.Producto;
import org.puig.puigapi.service.ProductoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
public class ProductController extends PersistenceController<Producto, String> {
    public ProductController(ProductoService service){super(service);}
}
