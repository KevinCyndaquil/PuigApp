package org.puig.puigapi.controller.inside.finances;

import org.puig.puigapi.controller.PersistenceController;
import org.puig.puigapi.persistence.entity.finances.Venta;
import org.puig.puigapi.service.PersistenceService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("ventas/reparto")
public class VentaRepartoController
        extends PersistenceController<Venta.Reparto, String, Venta.Reparto.Post> {
    protected VentaRepartoController(PersistenceService<Venta.Reparto, String> service) {
        super(service);
    }
}
