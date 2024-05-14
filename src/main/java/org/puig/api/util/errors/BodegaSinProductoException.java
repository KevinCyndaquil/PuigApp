package org.puig.api.util.errors;

import lombok.NonNull;
import org.puig.api.persistence.entity.admin.Proveedor;

public class BodegaSinProductoException extends RuntimeException {
    public BodegaSinProductoException(@NonNull Proveedor.Producto producto) {
        super("No se pueden agregar existencias de %s porque en la bodega no se ha recepcionado"
                .formatted(producto.getNombre()));
    }
}
