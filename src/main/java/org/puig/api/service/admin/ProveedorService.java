package org.puig.api.service.admin;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.puig.api.persistence.entity.admin.Proveedor;
import org.puig.api.persistence.repository.admin.ProveedorRepository;
import org.puig.api.service.PersistenceService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProveedorService implements PersistenceService<Proveedor> {
    final ProveedorRepository repository;

    @Override
    public @NonNull ProveedorRepository repository() {
        return repository;
    }

    @Override
    public @NonNull Class<Proveedor> clazz() {
        return Proveedor.class;
    }
}
