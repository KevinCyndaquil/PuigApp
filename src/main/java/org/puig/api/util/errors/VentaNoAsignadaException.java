package org.puig.api.util.errors;

import lombok.NonNull;
import org.puig.api.persistence.entity.finances.Venta;
import org.puig.api.persistence.entity.operation.Empleado;

public class VentaNoAsignadaException extends RuntimeException {
    public VentaNoAsignadaException(@NonNull Venta venta, @NonNull Empleado empleado) {
        super("Venta de id %s no pudo ser asignada a %s %s"
                .formatted(venta.getId(), empleado.getNombre(), empleado.getPuesto()));
    }
}
