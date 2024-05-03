package org.puig.puigapi.controller.inside.admin;

import org.bson.types.ObjectId;
import org.puig.puigapi.controller.PersistenceController;
import org.puig.puigapi.persistence.entity.admin.Proveedor;
import org.puig.puigapi.service.admin.ProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/proveedores")
public class ProvedorController extends PersistenceController<Proveedor, ObjectId, Proveedor.PostRequest> {
    @Autowired
    public ProvedorController(ProveedorService service) {
        super(service);
    }
}
