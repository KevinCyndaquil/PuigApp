package org.puig.puigapi.persistence.repositories.admin;

import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.persistence.repositories.PuigRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static org.puig.puigapi.persistence.entity.admin.Proveedor.*;

@Repository
public interface FacturaProveedorRepository
        extends PuigRepository<Factura, String> {

    @Query("{recepcion: ?0}")
    @NotNull List<Factura> findByFecha(@NotNull LocalDate fecha);
}
