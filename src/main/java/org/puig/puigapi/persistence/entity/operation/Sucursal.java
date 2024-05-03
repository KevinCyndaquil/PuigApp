package org.puig.puigapi.persistence.entity.operation;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.puig.puigapi.exceptions.BodegaSinProductoException;
import org.puig.puigapi.persistence.entity.admin.Proveedor;
import org.puig.puigapi.util.data.Direccion;
import org.puig.puigapi.util.contable.Contable;
import org.puig.puigapi.util.persistence.Instantiator;
import org.puig.puigapi.util.persistence.Irrepetibe;
import org.puig.puigapi.util.persistence.UniqueName;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalTime;
import java.util.HashSet;
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
public class Sucursal implements Irrepetibe<String>, UniqueName<String> {
    @Id private String id;
    private String nombre;
    private Direccion ubicacion;
    private LocalTime hora_abre;
    private LocalTime hora_cierra;
    private Bodega bodega = new Bodega();
    private Set<Empleado.Detalle> empleados = new HashSet<>();

    public void generar(@NonNull Empleado empleado, Empleado.Estados estado) {
        //if(empleados.add(empleado.generarAlta(estado))) return;

        empleados.stream()
                .reduce((a, d) -> d.getEmpleado().equals(empleado) ? d : a)
                .ifPresentOrElse(e -> e.setEstado(estado), () -> empleados.add(new Empleado.Detalle(empleado, estado)));
    }

    public void alta(@NonNull Empleado empleado) {
        generar(empleado, Empleado.Estados.ALTA);
    }

    @Data
    @NoArgsConstructor
    public static class PostRequest implements Instantiator<Sucursal> {
        @NotBlank(message = "Se requiere un nombre para la sucursal")
        @Pattern(regexp = "(?U)^[\\p{Lu}\\p{M}\\d]+( [\\p{Lu}\\p{M}\\d]+)*$",
                message = "Nombre de sucursal no es válido")
        private String nombre;
        @NotNull(message = "Se requiere la ubicación de la sucursal")
        @Valid
        private Direccion.PostRequest ubicacion;
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

    public static class Bodega extends HashSet<Contable<Proveedor.Producto>> {

        /**
         * Agrega un producto a esta sucursal, en caso de que el producto ya haya sido recepcionado
         * se agregaran las cantidades repartidas.
         * @param contable objeto que contiene el producto y la cantidad a recepcionar
         */
        public void recepcionar(Contable<Proveedor.Producto> contable) {
            if (add(new Contable<>(contable))) return;

            stream().reduce((a, p) -> p.equals(contable) ? p : a)
                    .ifPresent(p -> p.plus(contable.getCantidad()));
        }

        /**
         * Busca un producto en bodega y realiza una devolución a esta sucursal.
         * @param contable objeto que contiene el producto y la cantidad a agregar
         */
        public void agregarDevolucion(@NonNull Contable<Proveedor.Producto> contable) {
            getBy(contable.getDetalle())
                    .map(p -> p.getDetalle().isInventariado() ? p.plus(contable.getCantidad()) : p)
                    .orElseThrow(() -> new BodegaSinProductoException(contable.getDetalle()));
        }

        /**
         * Busca un producto en bodega y sustrae la cantidad de ese producto de esta sucursal.
         * @param contable objeto que contiene el producto y la cantidad a agregar.
         */
        public void quitarExistencias(@NonNull Contable<Proveedor.Producto> contable) {
            getBy(contable.getDetalle())
                    .map(p -> p.getDetalle().isInventariado() ? p.minus(contable.getCantidad()) : p)
                    .orElseThrow(() -> new BodegaSinProductoException(contable.getDetalle()));
        }

        /**
         * Obtiene una existencia buscando por el producto asignado.
         * @param producto el producto que se buscará en la bodega.
         * @return el objeto Contable del producto en caso de ser encontrado.
         */
        public Optional<Contable<Proveedor.Producto>> getBy(Proveedor.Producto producto) {
            return stream().reduce((a, p) -> p.equals(producto) ? p : a);
        }

        /**
         * Obtiene la cantidad de existencias buscando por el producto asignado.
         * @param producto el producto que se buscará en la bodega.
         * @return la cantidad de existencias del producto, en caso de no encontrar retornará 0.
         */
        public double getExistencias(Proveedor.Producto producto) {
            return getBy(producto)
                    .map(Contable::getCantidad)
                    .orElse(0d);
        }
    }
}
