package org.puig.puigapi.service.finances;

import org.puig.puigapi.persistence.entity.finances.Combo;
import org.puig.puigapi.persistence.repositories.finances.ComboRepository;
import org.puig.puigapi.service.PersistenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComboService extends PersistenceService<Combo, String> {

    @Autowired
    public ComboService(ComboRepository repository) {
        super(repository, Combo.class);
    }

    @Override
    public List<Combo> readAll() {
        return repository.findAllByClass(Combo.class.getCanonicalName());
    }
}
