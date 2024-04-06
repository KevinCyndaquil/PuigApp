package org.puig.puigapi.controller.inside.operation;

import org.puig.puigapi.controller.PersistenceController;
import org.puig.puigapi.persistence.entity.operation.Sucursal;
import org.puig.puigapi.service.operation.SucursalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sucursales")
public class SucursalController extends PersistenceController<Sucursal, String, Sucursal.Post> {
    @Autowired
    public SucursalController(SucursalService service) {
        super(service);
    }
}
