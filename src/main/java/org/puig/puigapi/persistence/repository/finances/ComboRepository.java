package org.puig.puigapi.persistence.repository.finances;

import org.puig.puigapi.persistence.entity.finances.Combo;
import org.puig.puigapi.persistence.repository.PuigRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComboRepository
        extends PuigRepository<Combo, String> {
}
