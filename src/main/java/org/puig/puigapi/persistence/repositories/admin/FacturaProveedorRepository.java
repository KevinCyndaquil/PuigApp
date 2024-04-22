package org.puig.puigapi.persistence.repositories.admin;

import jakarta.validation.constraints.NotNull;
import org.puig.puigapi.persistence.repositories.PuigRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static org.puig.puigapi.persistence.entity.admin.Proveedor.*;

@Repository
public interface FacturaProveedorRepository
        extends PuigRepository<Factura, String> {

    @Query("{recepcion: {$gte: ?0, $lte: ?1}}")
    @NotNull List<Factura> findByFecha(@NotNull LocalDateTime from, @NotNull LocalDateTime to);
}
