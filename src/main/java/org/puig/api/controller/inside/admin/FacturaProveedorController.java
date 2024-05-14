package org.puig.api.controller.inside.admin;

import lombok.RequiredArgsConstructor;
import org.puig.api.controller.PersistenceController;
import org.puig.api.controller.responses.ObjectResponse;
import org.puig.api.controller.responses.Response;
import org.puig.api.persistence.entity.admin.Proveedor;
import org.puig.api.service.admin.FacturaProveedorService;
import org.puig.api.util.PuigLogger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("proveedores/facturas")
@RequiredArgsConstructor
public class FacturaProveedorController implements PersistenceController<Proveedor.Factura> {
    final FacturaProveedorService service;
    final PuigLogger logger = new PuigLogger(FacturaProveedorController.class);

    @Override
    public FacturaProveedorService service() {
        return service;
    }

    @Override
    public PuigLogger logger() {
        return logger;
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
