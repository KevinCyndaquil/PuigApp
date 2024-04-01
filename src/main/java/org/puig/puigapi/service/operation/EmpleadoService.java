package org.puig.puigapi.service.operation;

import org.puig.puigapi.persistence.entity.operation.Empleado;
import org.puig.puigapi.persistence.repositories.operation.EmpleadoRepository;
import org.puig.puigapi.service.AuthService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpleadoService extends AuthService<Empleado> {
    public EmpleadoService(EmpleadoRepository repository) {
        super(repository, Empleado.class);
    }

    @Override
    public List<Empleado> readAll() {
        return repository.findAllByClass(Empleado.class.getCanonicalName());
    }
}
