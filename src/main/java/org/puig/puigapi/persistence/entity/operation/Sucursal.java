package org.puig.puigapi.persistence.entity.operation;

import com.mongodb.lang.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.puig.puigapi.persistence.entity.admin.Proveedor;
import org.puig.puigapi.persistence.entity.finances.ArticuloMenu;
import org.puig.puigapi.persistence.entity.utils.Direccion;
import org.puig.puigapi.persistence.entity.utils.persistence.Irrepetibe;
import org.puig.puigapi.persistence.entity.utils.persistence.PostEntity;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
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
    private Bodega bodega = new Bodega();
    private Set<Empleado.Detalle> empleados = new HashSet<>();

    /**
     * Agrega Productos a esta sucursal, si el producto ya existe en la tienda, unicamente agrega
     * la cantidad recepcionada a la cantidad ya existente.
     * @param producto el producto ingresado.
     * @param cantidad la cantidad por pieza ingresada.
     */
    public void agregarExistencia(@NonNull Sucursal.Producto producto,
                                  double cantidad)  {
        Bodega.Producto productoBodega =
                bodega.insert(new Bodega.Producto(producto, cantidad));

        if (Objects.isNull(productoBodega)) return;
        productoBodega.cantidad += cantidad;
    }

    /**
     * Realiza lo mismo que void agregarExitencias(producto, cantidad) pero este se basa en un objeto
     * porcion.
     * @param porcion la porcion que contiene el producto y la cantidad a agregar
     */
    public void agregarExistencias(@NonNull ArticuloMenu.Porcion porcion) {
        agregarExistencia(porcion.getProducto(), porcion.getCantidad());
    }

    public void quitarExistencias(@NonNull Sucursal.Producto producto,
                                  double cantidad) {
        Optional<Bodega.Producto> productoBodega = bodega.getBy(producto);

        if (productoBodega.isEmpty()) return;
        productoBodega.get().cantidad -= cantidad;
    }

    public void quitarExistencias(@NonNull ArticuloMenu.Porcion porcion) {
        quitarExistencias(porcion.getProducto(), porcion.getCantidad());
    }

    public void generar(@NonNull Empleado empleado, Empleado.EstadosEmpresa estado) {
        if(empleados.add(empleado.generarDetalle(estado))) return;

        Optional<Empleado.Detalle> detalle = empleados.stream()
                .reduce((a, d) -> d.getEmpleado().equals(empleado) ? d : a);

        detalle.orElseThrow(() -> new RuntimeException("Ocurrio un error inesperado durante la actualización de un empleado %s en la tienda %s"
                        .formatted(empleado.getNombre(), nombre)))
                .setEstado(estado);
    }

    public void alta(@NonNull Empleado empleado) {
        generar(empleado, Empleado.EstadosEmpresa.ALTA);
    }

    @Data
    @NoArgsConstructor
    public static class Request implements PostEntity<Sucursal> {
        @NotBlank(message = "Se requiere un nombre para la sucursal")
        @Pattern(regexp = "(?U)^[\\p{Lu}\\p{M}\\d]+( [\\p{Lu}\\p{M}\\d]+)*$",
                message = "Nombre de sucursal no es válido")
        private String nombre;
        @NotNull(message = "Se requiere la ubicación de la sucursal")
        @Valid
        private Direccion.RequestUsuario ubicacion;
        @NotNull(message = "Se requiere la hora de apertura de la sucursal")
        private LocalTime hora_abre;
        @NotNull(message = "Se requiere la hora de cierre de la sucursal")
        private LocalTime hora_cierra;

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
        public Producto(@org.jetbrains.annotations.NotNull Proveedor.Producto producto_proveedor,
                        boolean inventariado) {
            this.producto_proveedor = producto_proveedor;
            this.inventariado = inventariado;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Proveedor.Producto productoProveedor)
                return this.producto_proveedor.equals(productoProveedor);
            if (obj instanceof Sucursal.Producto productoSucursal)
                return codigo.equals(productoSucursal.getCodigo()) ||
                        producto_proveedor.equals(productoSucursal.getProducto_proveedor());
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
        public static class Request implements PostEntity<Producto> {
            @NotNull(message = "Se requiere el id del producto de proveedor")
            private Proveedor.Producto producto_proveedor;
            private boolean inventariado = true;

            @Override
            public Producto instance() {
                return Producto.builder()
                        .producto_proveedor(producto_proveedor)
                        .inventariado(inventariado)
                        .build();
            }
        }
    }

    public static class Bodega extends HashSet<Bodega.Producto> {

        public @Nullable Bodega.Producto insert(Bodega.Producto productoBodega) {
            if (add(productoBodega)) return null;
            return stream()
                    .reduce(null, (ac, b) -> b.equals(productoBodega) ? b : ac);
        }

        public Optional<Bodega.Producto> getBy(Sucursal.Producto productoSucursal) {
            return stream()
                    .reduce((ac, b) -> b.equals(productoSucursal) ? b : ac);
        }

        /**
         * Producto que se encuentra en la bodega de la sucursal, por lo que se encuentra en
         * cantidad.
         */
        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @EqualsAndHashCode(exclude = "cantidad")
        public static class Producto {
            /**
             * Producto en bodega.
             */
            protected Sucursal.Producto producto;
            /**
             * Cantidad por pieza del producto.
             */
            protected double cantidad;

            public boolean equals(Sucursal.Producto productoSucursal) {
                return this.producto.equals(productoSucursal);
            }

            @Override
            public boolean equals(Object obj) {
                if (obj instanceof Bodega.Producto productoBodega)
                    return this.producto.equals(productoBodega.producto);
                return false;
            }

            @Override
            public int hashCode() {
                return this.producto.hashCode();
            }
        }
    }
}
