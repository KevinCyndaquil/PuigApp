package org.puig.api.service.admin;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.puig.api.persistence.entity.admin.Proveedor;
import org.puig.api.persistence.repository.admin.ProductoProveedorRepository;
import org.puig.api.service.PersistenceService;
import org.puig.api.util.persistence.SimpleInstance;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProductoProveedorService implements PersistenceService<Proveedor.Producto> {
    final ProductoProveedorRepository repository;

    public Set<Proveedor.Producto> readByProveedor(@NonNull SimpleInstance proveedorInstance) {
        return repository.findByProveedorId(proveedorInstance.id());
    }

    @Override
    public @NonNull ProductoProveedorRepository repository() {
        return repository;
    }

    @Override
    public @NonNull Class<Proveedor.Producto> clazz() {
        return Proveedor.Producto.class;
    }
}
