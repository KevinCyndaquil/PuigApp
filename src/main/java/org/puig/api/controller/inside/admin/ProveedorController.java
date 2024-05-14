package org.puig.api.controller.inside.admin;

import lombok.RequiredArgsConstructor;
import org.puig.api.controller.PersistenceController;
import org.puig.api.persistence.entity.admin.Proveedor;
import org.puig.api.service.PersistenceService;
import org.puig.api.service.admin.ProveedorService;
import org.puig.api.util.PuigLogger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/proveedores")
@RequiredArgsConstructor
public class ProveedorController implements PersistenceController<Proveedor> {
    final ProveedorService service;
    final PuigLogger logger = new PuigLogger(ProveedorController.class);

    @Override
    public PersistenceService<Proveedor> service() {
        return service;
    }

    @Override
    public PuigLogger logger() {
        return logger;
    }
}
