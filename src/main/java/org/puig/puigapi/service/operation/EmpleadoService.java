package org.puig.puigapi.service.operation;

import org.puig.puigapi.persistence.entity.operation.Empleado;
import org.puig.puigapi.persistence.repositories.operation.EmpleadoRepository;
import org.puig.puigapi.service.AuthService;
import org.puig.puigapi.service.annotations.PuigService;

@PuigService(Empleado.class)
public class EmpleadoService extends
        AuthService<Empleado, EmpleadoRepository> {

    public EmpleadoService(EmpleadoRepository repository) {
        super(repository);
    }
}
