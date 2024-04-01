package org.puig.puigapi.persistence.entity.admin;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.persistence.entity.utils.Irrepetibe;
import org.puig.puigapi.persistence.entity.utils.Presentacion;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Representa un producto que fue traido por un proveedor, que se encuentra en bodega.
 */

@Data
@NoArgsConstructor
@Document(collection = "admin")
public class ProductoTienda implements Irrepetibe<String> {
    @Id private String codigo;
    @NotNull private String nombre;
    @NotNull private Presentacion presentacion;
    private double cantidad_bodega = 0d;
    private boolean inventariado;

    @Builder
    @JsonCreator
    public ProductoTienda(@NotNull String codigo,
                          @NotNull Proveedor.Producto producto,
                          boolean inventariado) {
        this.codigo = codigo;
        this.nombre = producto.getNombre();
        this.presentacion = producto.getPresentacion();
        this.inventariado = inventariado;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Proveedor.Producto producto)
            return nombre.equals(producto.getNombre()) &&
                    presentacion.equals(producto.getPresentacion());

        if (obj instanceof ProductoTienda productoTienda)
            return codigo.equals(productoTienda.codigo) ||
                    (nombre.equals(productoTienda.getNombre()) &&
                    presentacion.equals(productoTienda.getPresentacion()));

        return false;
    }

    @Override
    public String getId() {
        return codigo;
    }
}
