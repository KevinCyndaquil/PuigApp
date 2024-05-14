package org.puig.api.persistence.repository.admin;

import jakarta.validation.constraints.NotNull;
import org.puig.api.persistence.repository.PuigRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static org.puig.api.persistence.entity.admin.Proveedor.*;

@Repository
public interface FacturaProveedorRepository extends PuigRepository<Factura> {

    @Query("{recepcion: {$gte: ?0, $lte: ?1}}")
    @NotNull List<Factura> findByFecha(@NotNull LocalDateTime from, @NotNull LocalDateTime to);
}
