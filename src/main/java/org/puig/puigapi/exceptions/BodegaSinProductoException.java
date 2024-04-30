package org.puig.puigapi.exceptions;

import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.persistence.entity.admin.Proveedor;

public class BodegaSinProductoException extends RuntimeException {
    public BodegaSinProductoException(@NotNull Proveedor.Producto producto) {
        super("No se pueden agregar existencias de %s porque en la bodega no se ha recepcionado"
                .formatted(producto.getNombre()));
    }
}
