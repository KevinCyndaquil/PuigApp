package org.puig.puigapi.service;

import org.puig.puigapi.persistence.entity.admin.Producto;
import org.puig.puigapi.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductoService extends PersistenceService<Producto, String>{
    @Autowired
    public ProductoService(ProductoRepository repository){super(repository);}
}
