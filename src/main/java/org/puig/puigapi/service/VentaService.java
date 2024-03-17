package org.puig.puigapi.service;

import org.puig.puigapi.persistence.entity.finances.Venta;
import org.puig.puigapi.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VentaService extends PersistenceService<Venta, String> {
    @Autowired
    public VentaService(VentaRepository repository){super(repository);}
}
