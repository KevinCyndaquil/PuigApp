package org.puig.puigapi.controller.finances;

import org.puig.puigapi.controller.PersistenceController;
import org.puig.puigapi.persistence.entity.finances.Venta;
import org.puig.puigapi.service.VentaService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sell")
public class SellController extends PersistenceController<Venta, String> {
    public SellController(VentaService service) {
        super(service);
    }
}
