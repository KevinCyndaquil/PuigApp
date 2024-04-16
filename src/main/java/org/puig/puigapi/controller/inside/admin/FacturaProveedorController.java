package org.puig.puigapi.controller.inside.admin;

import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.controller.PersistenceController;
import org.puig.puigapi.controller.responses.ObjectResponse;
import org.puig.puigapi.controller.responses.Response;
import org.puig.puigapi.persistence.entity.admin.Proveedor;
import org.puig.puigapi.service.admin.FacturaProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("proveedores/facturas")
public class FacturaProveedorController
        extends PersistenceController<Proveedor.Factura, String, Proveedor.Factura.Request> {

    protected FacturaProveedorService service;

    @Autowired
    protected FacturaProveedorController(FacturaProveedorService service) {
        super(service);
        this.service = service;
    }

    @Override
    public ResponseEntity<Response> save(Proveedor.Factura.@NotNull Request request) {
        var factura = service.save(request.instance());

        return ObjectResponse.builder()
                .status(HttpStatus.CREATED)
                .message("hola")
                .body(factura)
                .build()
                .transform();
    }
}
