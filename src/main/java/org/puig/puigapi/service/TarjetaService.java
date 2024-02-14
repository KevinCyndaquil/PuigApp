package org.puig.puigapi.service;

import org.puig.puigapi.persistence.entity.utils.Tarjeta;
import org.puig.puigapi.repository.TarjetaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TarjetaService extends PersistenceService<Tarjeta, String>{
    @Autowired
    public TarjetaService(TarjetaRepository repository){super(repository);}
}
