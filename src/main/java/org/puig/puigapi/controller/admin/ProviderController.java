package org.puig.puigapi.controller.admin;

import org.puig.puigapi.controller.PersistenceController;
import org.puig.puigapi.persistence.entity.admin.Proveedor;
import org.puig.puigapi.service.ProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/provider")
public class ProviderController extends PersistenceController<Proveedor, String> {
    @Autowired
    public ProviderController(ProveedorService service){super(service);}
}
