package org.puig.puigapi.persistence.entity.operation;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.persistence.entity.admin.Proveedor;
import org.puig.puigapi.persistence.entity.utils.Direccion;
import org.puig.puigapi.persistence.entity.utils.persistence.Irrepetibe;
import org.puig.puigapi.persistence.entity.utils.persistence.PostEntity;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Local o edificio que se encuentra ubicada para realizar la venta_request de productos y alimentos
 * Puig.
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"ubicacion", "hora_abre", "hora_cierra"})
@Document(collection = "finances")
public class Sucursal implements Irrepetibe<String> {
    @Id private String id;
    private String nombre;
    private Direccion ubicacion;
    private LocalTime hora_abre;
    private LocalTime hora_cierra;
    private Set<Bodega> bodega = new HashSet<>();
    private Set<Empleado.Detalle> empleados = new HashSet<>();

    /**
     * Agrega Productos a esta sucursal, si el producto ya existe en la tienda, unicamente agrega
     * la cantidad recepcionada a la cantidad ya existente.
     * @param producto el producto ingresado.
     * @param cantidad la cantidad ingresada.
     */
    public void agregarExistencia(@NotNull Sucursal.Producto producto, int cantidad) {
        if (bodega.add(new Bodega(producto, cantidad))) return;
        bodega.forEach(d -> {
            if (d.getProducto().equals(producto)) d.setCantidad(d.getCantidad() + cantidad);
        });
    }

    public boolean quitarExistencias(@NotNull Sucursal.Producto producto, int cantidad) {
        if (!bodega.contains(new Bodega(producto, cantidad))) return true;
        bodega.forEach(d -> {
            if (d.getProducto().equals(producto)) d.setCantidad(d.getCantidad() - cantidad);
        });
        return true;
    }

    @Data
    @NoArgsConstructor
    public static class Request implements PostEntity<Sucursal> {
        @NotBlank(message = "Nombre de sucursal no es válido")
        @Pattern(regexp = "^[0-9A-Z ]+$",
                message = "Nombre de sucursal no es válido")
        private String nombre;
        @NotNull private Direccion.Request ubicacion;
        @NotNull @JsonFormat(pattern = "HH:mm:ss") private LocalTime hora_abre;
        @NotNull @JsonFormat(pattern = "HH:mm:ss") private LocalTime hora_cierra;

        @Override
        public Sucursal instance() {
            return Sucursal.builder()
                    .nombre(nombre)
                    .ubicacion(ubicacion.instance())
                    .hora_abre(hora_abre)
                    .hora_cierra(hora_cierra)
                    .build();
        }
    }

    /**
     * Representa un producto que fue traido por un proveedor, que se encuentra en bodega.
     */

    @Data
    @NoArgsConstructor
    @Document(collection = "operation")
    public static class Producto implements Irrepetibe<String> {
        @Id private String codigo;
        @DBRef(lazy = true) private Proveedor.Producto producto_proveedor;
        private boolean inventariado;

        @Builder
        public Producto(@NotNull Proveedor.Producto producto_proveedor, boolean inventariado) {
            this.producto_proveedor = producto_proveedor;
            this.inventariado = inventariado;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Proveedor.Producto pro_proveedor)
                return this.producto_proveedor.equals(pro_proveedor);
            if (obj instanceof Sucursal.Producto pro_tienda)
                return codigo.equals(pro_tienda.getCodigo()) ||
                        producto_proveedor.equals(pro_tienda.getProducto_proveedor());
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

        @Data
        public static class Request implements PostEntity<Sucursal.Producto> {
            @NotNull
            private Proveedor.Producto producto_proveedor;
            private boolean inventariado = true;

            @Override
            public Sucursal.Producto instance() {
                return Sucursal.Producto.builder()
                        .producto_proveedor(producto_proveedor)
                        .inventariado(inventariado)
                        .build();
            }
        }
    }

    /**
     * Producto que se encuentra en la bodega de la sucursal, por lo que se encuentra en
     * cantidad.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(exclude = "cantidad")
    public static class Bodega {
        @DBRef(lazy = true) private Producto producto;
        private int cantidad;
    }
}
