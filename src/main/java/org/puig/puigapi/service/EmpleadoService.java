package org.puig.puigapi.service;

import org.puig.puigapi.persistence.entity.operation.Empleado;
import org.puig.puigapi.persistence.entity.operation.Persona;
import org.puig.puigapi.repository.EmpleadoRepository;
import org.springframework.stereotype.Service;

@Service
public class EmpleadoService extends PersistenceService<Empleado, Persona>{
    public EmpleadoService(EmpleadoRepository repository){super(repository);}
}
