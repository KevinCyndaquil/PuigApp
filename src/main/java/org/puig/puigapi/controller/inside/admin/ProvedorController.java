package org.puig.puigapi.controller.inside.admin;

import org.puig.puigapi.controller.PersistenceController;
import org.puig.puigapi.persistence.entity.admin.ProductoTienda;
import org.puig.puigapi.persistence.entity.admin.Proveedor;
import org.puig.puigapi.service.admin.ProductoService;
import org.puig.puigapi.service.admin.ProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/proveedores")
public class ProvedorController extends PersistenceController<Proveedor, String> {
    @Autowired
    public ProvedorController(ProveedorService service) {
        super(service);
    }

    @RestController
    @RequestMapping("/proveedores/producto")
    public static class ProductoController extends PersistenceController<ProductoTienda, String> {

        @Autowired
        public ProductoController(ProductoService service) {
            super(service);
        }
    }
}
