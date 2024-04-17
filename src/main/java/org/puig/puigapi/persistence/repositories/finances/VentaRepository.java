package org.puig.puigapi.persistence.repositories.finances;

import jakarta.validation.constraints.NotNull;
import org.puig.puigapi.persistence.entity.finances.Venta;
import org.puig.puigapi.persistence.repositories.PuigRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VentaRepository
        extends PuigRepository<Venta, String> {

    @Query("{fecha_venta: ?0}")
    @NotNull List<Venta> findByFecha(@NotNull LocalDate from);

    @Query("{fecha_venta: {$gte: ?0, $lte: ?1}}")
    @NotNull List<Venta> findByFecha(@NotNull LocalDate from, @NotNull LocalDate to);
}