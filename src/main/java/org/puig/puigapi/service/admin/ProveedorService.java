package org.puig.puigapi.service.admin;

import org.puig.puigapi.persistence.entity.admin.Proveedor;
import org.puig.puigapi.persistence.repositories.admin.ProveedorFacturaRepository;
import org.puig.puigapi.persistence.repositories.admin.ProveedorRepository;
import org.puig.puigapi.service.PersistenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProveedorService extends PersistenceService<Proveedor, String> {
    @Autowired
    public ProveedorService(ProveedorRepository repository) {
        super(repository, Proveedor.class);
    }

    @Override
    public List<Proveedor> readAll() {
        return repository.findAllByClass(Proveedor.class.getCanonicalName());
    }

    @Service
    public static class Factura extends PersistenceService<Proveedor.Factura, String> {

        @Autowired
        public Factura(ProveedorFacturaRepository repository) {
            super(repository, Proveedor.Factura.class);
        }

        @Override
        public List<Proveedor.Factura> readAll() {
            return repository.findAllByClass(Proveedor.Factura.class.getCanonicalName());
        }
    }
}
