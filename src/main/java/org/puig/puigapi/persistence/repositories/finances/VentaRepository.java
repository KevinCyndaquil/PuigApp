package org.puig.puigapi.persistence.repositories.finances;

import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.persistence.entity.finances.Venta;
import org.puig.puigapi.persistence.repositories.PuigRepository;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VentaRepository
        extends PuigRepository<Venta, String> {

    @Query("{fecha_venta: {$gte: ?0, $lte: ?1}}")
    @NotNull List<Venta> findByFecha(@NotNull LocalDateTime from, @NotNull LocalDateTime to);

    @Aggregation("{ $match: {fecha_venta: {$gte: ?0, $lte: ?1}, forma_entrega: ?2}}")
    @NotNull List<Venta> findByFecha(@NotNull LocalDateTime from,
                                                  @NotNull LocalDateTime to,
                                                  @NotNull Venta.FormasEntrega filtro);
}