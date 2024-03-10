package org.puig.puigapi.controller.operation;

import org.puig.puigapi.controller.PersistenceController;
import org.puig.puigapi.persistence.entity.operation.Sucursal;
import org.puig.puigapi.persistence.entity.operation.Usuario;
import org.puig.puigapi.service.SucursalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/establishment")
public class SucursalController extends PersistenceController<Sucursal, String> {
    @Autowired
    public SucursalController(SucursalService service){super(service);}
}
