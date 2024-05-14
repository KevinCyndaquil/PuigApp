package org.puig.api.persistence.entity.admin;

import com.fasterxml.jackson.annotation.JsonGetter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import org.bson.types.ObjectId;
import org.puig.api.persistence.entity.operation.Sucursal;
import org.puig.api.util.*;
import org.puig.api.util.contable.Calculable;
import org.puig.api.util.contable.ObjetoConPrecio;
import org.puig.api.util.contable.Tarjeta;
import org.puig.api.util.data.Correo;
import org.puig.api.util.data.Direccion;
import org.puig.api.util.data.RFC;
import org.puig.api.util.data.Telefono;
import org.puig.api.util.grupos.InitInfo;
import org.puig.api.util.grupos.SimpleInfo;
import org.puig.api.util.grupos.UniqueInfo;
import org.puig.api.util.persistence.Unico;
import org.puig.api.util.persistence.Nombrable;
import org.puig.api.util.sat.RazonesSociales;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Es la persona, empresa o sitio que proporciona la materia prima para realizar los productos
 * del menú.
 */

@Data
@Document(collection = "admin")
public class Proveedor implements Unico, Nombrable {
    @Null(groups = SimpleInfo.class) @NotNull(groups = UniqueInfo.class)
    private ObjectId id;
    @NotBlank(message = "Se requiere un nombre para el proveedor", groups = InitInfo.class)
    @Pattern(regexp = "(?U)^[\\p{Lu}\\p{M}\\d]+( [\\p{Lu}\\p{M}\\d]+)*$",
            message = "Formato de nombre incorrecto, recuerda usar solo mayúsculas y letras",
            groups = InitInfo.class)
    private String nombre;
    @NotNull(message = "Se requiere un número de telefono fijo", groups = InitInfo.class)
    private @Valid Telefono telefono_fijo;
    @NotNull(message = "Se requiere un número de telefono movil", groups = InitInfo.class)
    private @Valid Telefono telefono_movil;
    @NotNull(message = "Se requiere un rfc para el proveedor", groups = InitInfo.class)
    private @Valid RFC rfc;
    private @Valid Correo correo;
    private @Valid Direccion ubicacion;
    @NotNull(message = "Se requiere la razón social para el proveedor", groups = InitInfo.class)
    private RazonesSociales razon = RazonesSociales.FISICO;
    @NotEmpty(message = "Se debe adjuntar al menos una cuenta de banco para el proveedor", groups = InitInfo.class)
    private Set<Tarjeta> cuentas;

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Proveedor proveedor)) return false;
        if (Objects.isNull(id)) return nombre.equals(proveedor.nombre);
        return id.equals(proveedor.id) || nombre.equals(proveedor.nombre);
    }

    @Override
    public int hashCode() {
        return id == null ? nombre.hashCode() : id.hashCode();
    }

    /**
     * Es un comprobante oficial o no que contiene un ticket de la materia proporcionada así cómo
     * quién la proporciono y su costo.
     */

    @Data
    @Document(collection = "admin")
    public static class Factura implements Unico {
        @Null(groups = SimpleInfo.class) @NotNull(groups = UniqueInfo.class)
        private ObjectId id;
        private String folio;
        @NotNull(message = "Se requiere el id del proveedor de la factura",
                groups = InitInfo.class)
        private @DBRef Proveedor proveedor;
        @NotEmpty(message = "Se deben ingresar productos a una factura para su creación",
                groups = InitInfo.class)
        private @Valid Set<Factura.Detalle> detalle = new HashSet<>();
        @NotNull(message = "Se requiere la fecha de recepción de la factura",
                groups = InitInfo.class)
        private LocalDateTime recepcion = LocalDateTime.now();
        @NotNull(message = "Se requiere el id de sucursal en la que se recibió la factura",
                groups = InitInfo.class)
        private @DBRef(lazy = true) Sucursal sucursal;

        @Field("total_iva")
        @JsonGetter("total_iva")
        public double getTotal_iva() {
            return detalle.stream()
                    .map(Detalle::getIva)
                    .reduce(0d, Double::sum);
        }

        @Field("monto_total")
        @JsonGetter("monto_total")
        public double getMonto_total() {
            return detalle.stream()
                    .map(Detalle::getMonto)
                    .reduce(0d, Double::sum);
        }

        @Field("monto")
        @JsonGetter("monto")
        public double getMonto() {
            return getMonto_total() - getTotal_iva();
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Factura factura)) return false;
            return id.equals(factura.id);
        }

        @Override
        public int hashCode() {
            return id.hashCode();
        }

        @Data
        @EqualsAndHashCode(callSuper = true)
        public static class Detalle extends Calculable<Producto> {
            @PositiveOrZero(groups = InitInfo.class)
            private double iva;

            public double getCantidadPresentacion() {
                Presentacion presentacion = detalle.getPresentacion();
                final double pesoPresentacion =
                        presentacion == null ? 1 : presentacion.getCantidad();
                return super.cantidad / pesoPresentacion;
            }

            @Override
            public double getMonto() {
                return getCantidadPresentacion() * detalle.getPrecio() + iva;
            }
        }
    }

    /**
     * Productos sumistrados y detallados por un proveedor.
     */

    @Data
    @Document(collection = "admin")
    public static class Producto implements Unico, Nombrable, ObjetoConPrecio {
        @Null(groups = SimpleInfo.class) @NotNull(groups = UniqueInfo.class)
        private ObjectId id;
        @NotNull(message = "Se requiere el id del proveedor del producto",
                groups = InitInfo.class)
        private @DBRef Proveedor proveedor;
        @NotBlank(message = "Se require un nombre para el producto de proveedor",
                groups = InitInfo.class)
        private String nombre;
        @Positive(groups = InitInfo.class)
        private double precio;
        @NotNull(message = "Se requiere la presentación del producto de proveedor",
                groups = InitInfo.class)
        private @Valid Presentacion presentacion;
        private boolean inventariado = true;

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Producto producto)) return false;
            if (id == null) return nombre.equals(producto.nombre) &&
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
    }
}