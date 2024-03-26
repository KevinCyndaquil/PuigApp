package org.puig.puigapi.service.operation;

import org.puig.puigapi.persistence.entity.operation.Empleado;
import org.puig.puigapi.persistence.entity.utils.Persona;
import org.puig.puigapi.persistence.repositories.operation.EmpleadoRepository;
import org.puig.puigapi.service.AuthService;
import org.springframework.stereotype.Service;

@Service
public class EmpleadoService extends AuthService<Empleado, Persona> {
    public EmpleadoService(EmpleadoRepository repository) {
        super(repository);
    }

}
