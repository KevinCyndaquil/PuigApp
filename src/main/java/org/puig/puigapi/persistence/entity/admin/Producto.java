package org.puig.puigapi.persistence.entity.admin;

import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "admin")
public class Producto extends Proveedor.Producto {
    private int cantidad_bodega;
    private boolean inventariado;

    public Producto(String _id,
                    Proveedor proveedor,
                    @NotNull String nombre,
                    float precio,
                    Proveedor.@NotNull Presentacion presentacion,
                    int cantidad_bodega,
                    boolean inventariado) {
        super(_id, proveedor, nombre, precio, presentacion);
        this.cantidad_bodega = cantidad_bodega;
        this.inventariado = inventariado;
    }

    @Data
    @AllArgsConstructor
    @EqualsAndHashCode(exclude = "cantidad")
    public static class Receta {
        @DBRef private @NotNull Producto producto;
        private float cantidad;
    }
}
