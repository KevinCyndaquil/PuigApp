package org.puig.puigapi.service;

import org.puig.puigapi.persistence.entity.finances.Combo;
import org.puig.puigapi.repository.ComboRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ComboService extends PersistenceService<Combo, String>{
    public ComboService(ComboRepository repository){super(repository);}
}
