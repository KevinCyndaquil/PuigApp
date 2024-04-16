package org.puig.puigapi.persistence.repositories.finances;

import org.puig.puigapi.persistence.entity.finances.Venta;
import org.puig.puigapi.persistence.repositories.PuigRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VentaRepository
        extends PuigRepository<Venta, String> {

    @Query("{$or: [{'_class': 'org.puig.puigapi.persistence.entity.finances.Venta'}, {'_class': 'org.puig.puigapi.persistence.entity.finances.Venta$Reparto'}]}")
    List<Venta> findAllVentas();
}