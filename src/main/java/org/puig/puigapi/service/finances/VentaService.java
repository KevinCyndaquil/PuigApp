package org.puig.puigapi.service.finances;

import org.puig.puigapi.persistence.entity.finances.Venta;
import org.puig.puigapi.persistence.repositories.finances.VentaRepartoRepository;
import org.puig.puigapi.persistence.repositories.finances.VentaRepository;
import org.puig.puigapi.service.PersistenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VentaService extends PersistenceService<Venta, String> {
    @Autowired
    public VentaService(VentaRepository repository) {
        super(repository, Venta.class);
    }

    @Override
    public List<Venta> readAll() {
        return repository.findAllByClass(Venta.class.getCanonicalName());
    }

    @Service
    public static class Reparto extends PersistenceService<Venta.Reparto, String> {

        @Autowired
        public Reparto(VentaRepartoRepository repository) {
            super(repository, Venta.Reparto.class);
        }

        @Override
        public List<Venta.Reparto> readAll() {
            return repository.findAllByClass(Venta.Reparto.class.getCanonicalName());
        }
    }
}
