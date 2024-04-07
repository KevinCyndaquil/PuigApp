package org.puig.puigapi.persistence.entity.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.persistence.entity.utils.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Es la persona, empresa o sitio que proporciona la materia prima para realizar los productos
 * del menú.
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "admin")
public class Proveedor implements Irrepetibe<String> {
    @Id private String id;
    private String nombre;
    private String telefono_fijo;
    private String telefono_movil;
    private String rfc;
    private String correo;
    private Direccion ubicacion;
    private RazonesSociales razon = RazonesSociales.MORAL;
    private Set<Tarjeta> cuentas = new HashSet<>();

    public enum RazonesSociales {
        FISICO,
        MORAL
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Proveedor proveedor)) return false;
        if (id == null)
            return nombre.equals(proveedor.nombre);
        return id.equals(proveedor.id) || nombre.equals(proveedor.nombre);
    }

    @Override
    public int hashCode() {
        return id == null ? nombre.hashCode() : id.hashCode();
    }

    public record Post(
            @NotBlank(message = "El nombre de proveedor no puede estar vacío")
            String nombre,
            @Pattern(regexp = "^\\+\\([0-9]{2}\\) [0-9]{3} [0-9]{3} [0-9]{4}$",
                    message = "Teléfono fijo de proveedor no es válido")
            String telefono_fijo,
            @Pattern(regexp = "^\\+\\([0-9]{2}\\) [0-9]{3} [0-9]{3} [0-9]{4}$",
                    message = "Teléfono móvil de proveedor no es válido")
            String telefono_movil,
            @Pattern(regexp = "^[a-zA-Z]{4}[0-9]{6}[a-zA-Z0-9]{3}$",
                    message = "RFC de proveedor no es válido")
            @NotBlank(message = "rfc de proveedor no puede estar vacío")
            String rfc,
            @Pattern(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$",
                    message = "Correo electrónico de proveedor no es válida")
            String correo,
            Direccion ubicacion,
            @NotNull RazonesSociales razon,
            @NotEmpty(message = "No debe estar vacío") Set<Tarjeta> cuentas
    ) implements PostEntity<Proveedor> {

        @Override
        public Proveedor instance() {
            return Proveedor.builder()
                    .nombre(nombre)
                    .telefono_fijo(telefono_fijo)
                    .telefono_movil(telefono_movil)
                    .rfc(rfc)
                    .correo(correo)
                    .ubicacion(ubicacion)
                    .razon(razon)
                    .build();
        }
    }

    /**
     * Es un comprobante oficial o no que contiene un detalle de la materia proporcionada así cómo
     * quién la proporciono y su costo.
     */
    @Data
    @NoArgsConstructor
    @EqualsAndHashCode(exclude = {
            "proveedor",
            "detalle",
            "recepcion",
            "monto",
            "iva",
            "monto_total"})
    @Document(collection = "admin")
    public static class Factura implements Irrepetibe<String> {
        @Id private String folio;
        @DBRef private Proveedor proveedor;
        private Set<Detalle<Producto>> detalle = new HashSet<>();
        private LocalDate recepcion;
        private double monto;
        private double iva;
        private double monto_total;

        public record Post(
                @NotBlank(message = "Folio de factura no puede estar vacío")
                String folio,
                @NotNull Proveedor proveedor,
                @NotNull @NotEmpty(message = "Se deben ingresar productos a una factura para su creación")
                Set<Detalle<Producto>> detalle,
                @NotNull @JsonFormat(pattern = "yyyy-MM-dd")
                LocalDate recepcion,
                double iva
        ) implements PostEntity<Factura> {
            @Override
            public Factura instance() {
                return Factura.builder()
                        .folio(folio)
                        .proveedor(proveedor)
                        .detalle(detalle)
                        .recepcion(recepcion)
                        .iva(iva)
                        .build();
            }
        }

        @Builder
        public Factura(@NotNull String folio,
                       @NotNull Proveedor proveedor,
                       @NotNull Set<Detalle<Producto>> detalle,
                       @NotNull LocalDate recepcion,
                       double iva) {
            this.folio = folio;
            this.proveedor = proveedor;
            this.recepcion = recepcion;
            this.detalle = detalle;
            this.monto = detalle.stream()
                    .map(Detalle::getMonto)
                    .reduce(0d, Double::sum);
            this.iva = iva;
            this.monto_total = monto + iva;
        }

        @Override
        public String getId() {
            return folio;
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Document(collection = "admin")
    public static class Producto
            implements Irrepetibe<String>, ObjetoConPrecio {

        @Id private String id;
        @DBRef private Proveedor proveedor;
        private String nombre;
        private double precio;
        private Presentacion presentacion;

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Producto producto)) return false;
            if (id == null)
                return nombre.equals(producto.nombre) &&
                        presentacion.equals(producto.presentacion);
            return id.equals(producto.id) ||
                    (nombre.equals(producto.nombre) &&
                            presentacion.equals(producto.presentacion));
        }

        @Override
        public int hashCode() {
            return id == null ?
                    Objects.hash(nombre, presentacion) :
                    id.hashCode() + Objects.hash(nombre, presentacion);
        }

        public record Post(
                @NotNull Proveedor proveedor,
                @NotBlank(message = "Nombre de producto proveedor no es válido")
                String nombre,
                @Positive(message = "El precio debe ser mayor a cero")
                double precio,
                @NotNull Presentacion presentacion
        ) implements PostEntity<Producto> {

            @Override
            public Producto instance() {
                return Producto.builder()
                        .proveedor(proveedor)
                        .nombre(nombre)
                        .precio(precio)
                        .presentacion(presentacion)
                        .build();
            }
        }
    }
}