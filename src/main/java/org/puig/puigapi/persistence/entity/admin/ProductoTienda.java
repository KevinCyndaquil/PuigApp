package org.puig.puigapi.persistence.entity.admin;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.persistence.entity.utils.Irrepetibe;
import org.puig.puigapi.persistence.entity.utils.PostEntity;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Representa un producto que fue traido por un proveedor, que se encuentra en bodega.
 */

@Data
@NoArgsConstructor
@Document(collection = "admin")
public class ProductoTienda implements Irrepetibe<String> {
    @Id private String codigo;
    @DBRef private Proveedor.Producto producto_proveedor;
    private double cantidad_bodega = 0d;
    private boolean inventariado;

    @Builder
    public ProductoTienda(@NotNull String codigo,
                          @NotNull Proveedor.Producto producto_proveedor,
                          boolean inventariado) {
        this.codigo = codigo;
        this.producto_proveedor = producto_proveedor;
        this.inventariado = inventariado;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Proveedor.Producto pro_proveedor)
            return this.producto_proveedor.equals(pro_proveedor);
        if (obj instanceof ProductoTienda pro_tienda)
            return codigo.equals(pro_tienda.codigo) ||
                    producto_proveedor.equals(pro_tienda.producto_proveedor);
        return false;
    }

    @Override
    public int hashCode() {
        return codigo == null ? producto_proveedor.hashCode() : codigo.hashCode();
    }

    @Override
    public String getId() {
        return codigo;
    }

    public record Post(
            @NotBlank(message = "Código de producto tienda no debe estar vacío")
            String codigo,
            @NotNull Proveedor.Producto producto_proveedor,
            boolean inventariado
    ) implements PostEntity<ProductoTienda> {

        @Override
        public ProductoTienda instance() {
            return ProductoTienda.builder()
                    .codigo(codigo)
                    .producto_proveedor(producto_proveedor)
                    .inventariado(inventariado)
                    .build();
        }
    }
}