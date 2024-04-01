package org.puig.puigapi.controller.outside;

import org.puig.puigapi.controller.PersistenceController;
import org.puig.puigapi.persistence.entity.operation.Empleado;
import org.puig.puigapi.service.operation.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/empleado")
public class EmpleadoController extends PersistenceController<Empleado, String> {
    @Autowired
    public EmpleadoController(EmpleadoService service){super(service);}
}