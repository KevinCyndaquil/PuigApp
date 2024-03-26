package org.puig.puigapi.controller.inside.finances;

import org.puig.puigapi.controller.PersistenceController;
import org.puig.puigapi.persistence.entity.finances.Venta;
import org.puig.puigapi.service.finances.VentaService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ventas")
public class VentaController extends PersistenceController<Venta, String> {
    public VentaController(VentaService service) {
        super(service);
    }
}
