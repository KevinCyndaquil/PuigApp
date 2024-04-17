package org.puig.puigapi.persistence.entity.operation;

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
    public void agregarExistencia(@org.jetbrains.annotations.NotNull Sucursal.Producto producto,
                                  double cantidad) {
        if (bodega.add(new Bodega(producto, cantidad))) return;
        bodega.forEach(d -> {
            if (d.getProducto().equals(producto)) d.setCantidad(d.getCantidad() + cantidad);
        });
    }

    /**
     * Realiza lo mismo que void agregarExitencias(producto, cantidad) pero este se basa en un objeto
     * receta.
     * @param receta la receta que contiene el producto y la cantidad a agregar.
     */
    public void agregarExistencias(@org.jetbrains.annotations.NotNull ArticuloMenu.Receta receta) {
        agregarExistencia(receta.getProducto(), receta.getCantidad());
    }

    public void quitarExistencias(@org.jetbrains.annotations.NotNull Sucursal.Producto producto,
                                     double cantidad) {
        if (!bodega.contains(new Bodega(producto, cantidad))) return;
        bodega.forEach(d -> {
            if (d.getProducto().equals(producto)) d.setCantidad(d.getCantidad() - cantidad);
        });
    }

    public void quitarExistencias(@org.jetbrains.annotations.NotNull ArticuloMenu.Receta receta) {
        quitarExistencias(receta.getProducto(), receta.getCantidad());
    }

    public void generar(@org.jetbrains.annotations.NotNull Empleado empleado, Empleado.Estados estado) {
        if(empleados.add(empleado.generarDetalle(estado))) return;
        Empleado.Detalle detalle = empleados.stream()
                .reduce(null, (a, d) -> d.getEmpleado().equals(empleado) ? d : a);
        if (Objects.isNull(detalle))
            throw new RuntimeException("Ocurrio un nullpointer inesperado");
        detalle.setEstado(estado);
    }

    public void alta(@org.jetbrains.annotations.NotNull Empleado empleado) {
        generar(empleado, Empleado.Estados.ALTA);
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
            @NotNull(message = "Se requiere el id del producto de proveedor")
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
        private double cantidad;
    }
}
