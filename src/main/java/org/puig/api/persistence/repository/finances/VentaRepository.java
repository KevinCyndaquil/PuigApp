package org.puig.api.persistence.repository.finances;

import lombok.NonNull;
import org.puig.api.persistence.entity.finances.Venta;
import org.puig.api.persistence.repository.PuigRepository;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VentaRepository extends PuigRepository<Venta> {

    @Query("{fecha_venta: {$gte: ?0, $lte: ?1}}")
    @NonNull List<Venta> findByFecha(@NonNull LocalDateTime from, @NonNull LocalDateTime to);

    @Aggregation("{ $match: {fecha_venta: {$gte: ?0, $lte: ?1}, forma_entrega: ?2}}")
    @NonNull List<Venta> findByFecha(@NonNull LocalDateTime from,
                                     @NonNull LocalDateTime to,
                                     @NonNull Venta.ModosDeEntrega filtro);
}