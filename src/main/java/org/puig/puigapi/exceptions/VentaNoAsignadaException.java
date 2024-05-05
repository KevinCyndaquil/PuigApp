package org.puig.puigapi.exceptions;

import lombok.NonNull;
import org.puig.puigapi.persistence.entity.finances.Venta;
import org.puig.puigapi.persistence.entity.operation.Empleado;

public class VentaNoAsignadaException extends RuntimeException {
    public VentaNoAsignadaException(@NonNull Venta venta, @NonNull Empleado empleado) {
        super("Venta de id %s no pudo ser asignada a %s %s"
                .formatted(venta.getId(), empleado.getNombre(), empleado.getPuesto()));
    }
}
