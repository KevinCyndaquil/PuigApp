package org.puig.puigapi.controller.operation;

import org.puig.puigapi.controller.PersistenceController;
import org.puig.puigapi.persistence.entity.operation.Empleado;
import org.puig.puigapi.persistence.entity.operation.Persona;
import org.puig.puigapi.service.operation.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employee")
public class EmpleadoController extends PersistenceController<Empleado, Persona> {
    @Autowired
    public EmpleadoController(EmpleadoService service){super(service);}
}
