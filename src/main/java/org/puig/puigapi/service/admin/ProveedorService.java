package org.puig.puigapi.service.admin;

import org.puig.puigapi.persistence.entity.admin.Proveedor;
import org.puig.puigapi.persistence.repositories.admin.ProveedorFacturaRepository;
import org.puig.puigapi.persistence.repositories.admin.ProveedorProductoRepository;
import org.puig.puigapi.persistence.repositories.admin.ProveedorRepository;
import org.puig.puigapi.service.PersistenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProveedorService extends PersistenceService<Proveedor, String> {
    @Autowired
    public ProveedorService(ProveedorRepository repository) {
        super(repository);
    }

    @Service
    public static class Factura extends PersistenceService<Proveedor.Factura, String> {

        @Autowired
        public Factura(ProveedorFacturaRepository repository) {
            super(repository);
        }
    }

    @Service
    public static class Producto extends PersistenceService<Proveedor.Producto, String> {

        @Autowired
        public Producto(ProveedorProductoRepository repository) {
            super(repository);
        }
    }
}
