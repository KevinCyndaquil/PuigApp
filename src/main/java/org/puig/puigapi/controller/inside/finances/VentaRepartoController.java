package org.puig.puigapi.controller.inside.finances;

import org.puig.puigapi.controller.PersistenceController;
import org.puig.puigapi.persistence.entity.finances.Venta;
import org.puig.puigapi.service.PersistenceService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("ventas/repartos")
public class VentaRepartoController
        extends PersistenceController<Venta.Reparto, String, Venta.Reparto.Request> {
    protected VentaRepartoController(PersistenceService<Venta.Reparto, String> service) {
        super(service);
    }
}
