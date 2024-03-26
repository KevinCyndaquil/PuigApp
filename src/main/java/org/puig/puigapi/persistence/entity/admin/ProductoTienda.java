package org.puig.puigapi.persistence.entity.admin;

import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.persistence.entity.utils.Presentacion;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Representa un producto que fue traido por un proveedor, que se encuentra en bodega.
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"cantidad_bodega", "inventariado"})
@Document(collection = "admin")
public class ProductoTienda {
    @Id private String _codigo;
    private @NotNull String nombre;
    private @NotNull Presentacion presentacion;
    private float cantidad_bodega = 0f;
    private boolean inventariado = true;

    public ProductoTienda(@NotNull Proveedor.Producto producto) {
        this.nombre = producto.getNombre();
        this.presentacion = producto.getPresentacion();
    }

    public boolean equals(Proveedor.Producto producto) {
        if (producto == null) return false;
        return nombre.equals(producto.getNombre()) && presentacion.equals(producto.getPresentacion());
    }
}
