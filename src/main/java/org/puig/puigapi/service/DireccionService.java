package org.puig.puigapi.service;

import org.puig.puigapi.persistence.entity.utils.Direccion;
import org.puig.puigapi.repository.DireccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
public class DireccionService extends PersistenceService<Direccion, String>{
    @Autowired
    public DireccionService(DireccionRepository repository){super(repository);}
}
