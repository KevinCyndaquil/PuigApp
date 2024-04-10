package org.puig.puigapi.controller.inside.admin;

import org.puig.puigapi.controller.PersistenceController;
import org.puig.puigapi.persistence.entity.admin.Proveedor;
import org.puig.puigapi.service.admin.FacturaProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("proveedores/facturas")
public class FacturaProveedorController
        extends PersistenceController<Proveedor.Factura, String, Proveedor.Factura.Request> {

    @Autowired
    protected FacturaProveedorController(FacturaProveedorService service) {
        super(service);
    }
}