package org.puig.puigapi.service.finances;

import org.puig.puigapi.persistence.entity.finances.Combo;
import org.puig.puigapi.persistence.repository.finances.ComboRepository;
import org.puig.puigapi.service.PersistenceService;
import org.puig.puigapi.util.annotation.PuigService;

@PuigService(Combo.class)
public class ComboService extends PersistenceService<Combo, String, ComboRepository> {

    public ComboService(ComboRepository repository) {
        super(repository);
    }
}
