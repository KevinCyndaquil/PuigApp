package org.puig.puigapi.service.admin;

import org.puig.puigapi.persistence.entity.admin.ProductoTienda;
import org.puig.puigapi.persistence.repositories.admin.ProductoRepository;
import org.puig.puigapi.service.PersistenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductoService extends PersistenceService<ProductoTienda, String> {
    @Autowired
    public ProductoService(ProductoRepository repository) {
        super(repository);
    }
}
