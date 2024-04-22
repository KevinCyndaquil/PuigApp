package org.puig.puigapi.controller.inside.admin;

import org.puig.puigapi.controller.PersistenceController;
import org.puig.puigapi.controller.responses.ObjectResponse;
import org.puig.puigapi.controller.responses.Response;
import org.puig.puigapi.persistence.entity.admin.Proveedor;
import org.puig.puigapi.service.admin.FacturaProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

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

    @GetMapping(value = "where/date/is")
    public ResponseEntity<Response> readByDate(@RequestParam("from") LocalDate from) {
        var result = service.readByDate(from);

        return ObjectResponse.builder()
                .status(HttpStatus.OK)
                .message("Facturas del día %s leídas correctamente"
                        .formatted(from))
                .body(result)
                .build()
                .transform();
    }
}
