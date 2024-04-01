package org.puig.puigapi.service.admin;

import org.puig.puigapi.persistence.entity.admin.ProductoTienda;
import org.puig.puigapi.persistence.repositories.admin.ProductoRepository;
import org.puig.puigapi.service.PersistenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoTiendaService
        extends PersistenceService<ProductoTienda, String> {
    @Autowired
    public ProductoTiendaService(ProductoRepository repository) {
        super(repository, ProductoTienda.class);
    }

    @Override
    public List<ProductoTienda> readAll() {
        return repository.findAllByClass(ProductoTienda.class.getCanonicalName());
    }
}
