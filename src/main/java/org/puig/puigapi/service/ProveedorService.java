package org.puig.puigapi.service;

import org.puig.puigapi.persistence.entity.admin.Proveedor;
import org.puig.puigapi.repository.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProveedorService extends PersistenceService<Proveedor, String>{
    @Autowired
    public ProveedorService(ProveedorRepository repository){super(repository);}
}
