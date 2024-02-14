package org.puig.puigapi.service;

import org.puig.puigapi.persistence.entity.DatoBanco;
import org.puig.puigapi.repository.DatoBancoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DatoBancoService extends PersistenceService<DatoBanco, String>{
    @Autowired
    public DatoBancoService(DatoBancoRepository repository){super(repository);}
}
