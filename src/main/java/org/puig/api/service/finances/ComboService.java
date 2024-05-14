package org.puig.api.service.finances;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.puig.api.persistence.entity.finances.Combo;
import org.puig.api.persistence.repository.PuigRepository;
import org.puig.api.persistence.repository.finances.ComboRepository;
import org.puig.api.service.PersistenceService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ComboService implements PersistenceService<Combo> {
    final ComboRepository repository;

    @Override
    public @NonNull PuigRepository<Combo> repository() {
        return repository;
    }

    @Override
    public @NonNull Class<Combo> clazz() {
        return Combo.class;
    }
}
