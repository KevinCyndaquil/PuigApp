package org.puig.puigapi.persistence.entity.admin;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import org.bson.types.ObjectId;
import org.puig.puigapi.persistence.entity.operation.Sucursal;
import org.puig.puigapi.util.*;
import org.puig.puigapi.util.contable.Calculable;
import org.puig.puigapi.util.contable.ObjetoConPrecio;
import org.puig.puigapi.util.contable.Tarjeta;
import org.puig.puigapi.util.data.Correo;
import org.puig.puigapi.util.data.Direccion;
import org.puig.puigapi.util.data.RFC;
import org.puig.puigapi.util.data.Telefono;
import org.puig.puigapi.util.persistence.Instantiator;
import org.puig.puigapi.util.persistence.Irrepetibe;
import org.puig.puigapi.util.persistence.SimpleInstance;
import org.puig.puigapi.util.persistence.UniqueName;
import org.puig.puigapi.util.sat.RazonesSociales;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Es la persona, empresa o sitio que proporciona la materia prima para realizar los productos
 * del menú.
 */

@JsonIgnoreProperties(value = { "target", "source" })

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "admin")
public class Proveedor implements Irrepetibe<ObjectId>, UniqueName<String> {
    @Id private ObjectId id;
    private String nombre;
    private Telefono telefono_fijo;
    private Telefono telefono_movil;
    private RFC rfc;
    private Correo correo;
    private Direccion ubicacion;
    private RazonesSociales razon;
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

    @Data
    @NoArgsConstructor
    public static class PostRequest implements Instantiator<Proveedor> {
        @NotBlank(message = "Se requiere un nombre para el proveedor")
        @Pattern(regexp = "(?U)^[\\p{Lu}\\p{M}\\d]+( [\\p{Lu}\\p{M}\\d]+)*$",
                message = "Formato de nombre incorrecto, recuerda usar solo mayúsculas y letras")
        private String nombre;
        @Valid
        @NotNull(message = "Se requiere un número de telefono fijo")
        private Telefono telefono_fijo;
        @Valid
        private Telefono telefono_movil;
        @Valid
        @NotNull(message = "Se requiere un rfc para el proveedor")
        private RFC rfc;
        @Valid
        private Correo correo;
        @Valid
        private Direccion.ParaFactura ubicacion;
        @NotNull(message = "Se requiere una razon social para el proveedor")
        private RazonesSociales razon = RazonesSociales.MORAL;
        @Valid
        @NotEmpty(message = "Se debe adjuntar al menos una cuenta de banco para el proveedor")
        private Set<Tarjeta.Request> cuentas;

        @Override
        public Proveedor instance() {
            return Proveedor.builder()
                    .nombre(nombre)
                    .telefono_fijo(telefono_fijo)
                    .telefono_movil(telefono_movil)
                    .rfc(rfc)
                    .correo(correo)
                    .ubicacion(ubicacion.instance())
                    .razon(razon)
                    .cuentas(cuentas.stream()
                            .map(Instantiator::instance)
                            .collect(Collectors.toSet()))
                    .build();
        }
    }

    /**
     * Es un comprobante oficial o no que contiene un ticket de la materia proporcionada así cómo
     * quién la proporciono y su costo.
     */

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Document(collection = "admin")
    public static class Factura implements Irrepetibe<String> {
        @Id private String id;
        @DBRef private Proveedor proveedor;
        private Set<Factura.Detalle> detalle = new HashSet<>();
        private LocalDateTime recepcion = LocalDateTime.now();
        @DBRef(lazy = true) private Sucursal sucursal;

        @JsonProperty(access = Access.READ_ONLY) private double monto;
        @JsonProperty(access = Access.READ_ONLY) private double monto_total;
        @JsonProperty(access = Access.READ_ONLY) private double total_iva;

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Factura factura)) return false;
            return id.equals(factura.id);
        }

        @Override
        public int hashCode() {
            return id.hashCode();
        }

        @JsonGetter("total_iva")
        public double getTotal_iva() {
            return detalle.stream()
                    .map(Detalle::getIva)
                    .reduce(0d, Double::sum);
        }

        @JsonGetter("monto_total")
        public double getMonto_total() {
            return detalle.stream()
                    .map(Detalle::getMonto)
                    .reduce(0d, Double::sum);
        }

        @JsonGetter("monto")
        public double getMonto() {
            return getMonto_total() - getTotal_iva();
        }

        public void update() {
            this.monto = getMonto();
            this.total_iva = getTotal_iva();
            this.monto_total = getMonto_total();
        }

        @Data
        @NoArgsConstructor
        @EqualsAndHashCode(callSuper = true)
        public static class Detalle extends Calculable<Producto> {
            @PositiveOrZero(message = "Iva en el ticket factura debe ser mayor o igual a cero")
            private double iva;

            public double getCantidadPresentacion() {
                Presentacion presentacion = detalle.getPresentacion();
                final double pesoPresentacion =
                        presentacion == null ? 1 : presentacion.getPeso_total();
                return super.cantidad / pesoPresentacion;
            }

            @Override
            public double getMonto() {
                monto = getCantidadPresentacion() * detalle.getPrecio() + iva;
                return monto;
            }
        }

        @Data
        public static class PostRequest implements Instantiator<Factura> {
            @NotBlank(message = "Folio de factura no puede estar vacío")
            private String folio;
            @NotNull(message = "Se requiere el id del proveedor de la factura")
            private SimpleInstance<ObjectId> proveedor;
            @Valid
            @NotEmpty(message = "Se deben ingresar productos a una factura para su creación")
            private Set<Factura.Detalle> detalle;
            @NotNull(message = "Se requiere la sucursal en la que se recibió la factura")
            private SimpleInstance<String> sucursal;

            @Override
            public Factura instance() {
                return Factura.builder()
                        .id(folio)
                        .proveedor(proveedor.instance(Proveedor.class))
                        .detalle(detalle)
                        .sucursal(sucursal.instance(Sucursal.class))
                        .build();
            }
        }
    }

    /**
     * Productos sumistrados y detallados por un proveedor.
     */

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Document(collection = "admin")
    public static class Producto implements Irrepetibe<ObjectId>, ObjetoConPrecio, UniqueName<String> {
        @Id private ObjectId id;
        @DBRef private Proveedor proveedor;
        private String nombre;
        private double precio;
        private Presentacion presentacion;
        private boolean inventariado;

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

        @Data
        @NoArgsConstructor
        public static class PostRequest implements Instantiator<Producto> {
            @NotNull(message = "Se requiere el id del proveedor del producto")
            private SimpleInstance<ObjectId> proveedor;
            @NotBlank(message = "Se require un nombre para el producto de proveedor")
            private String nombre;
            @Positive(message = "El precio debe ser mayor a cero")
            private double precio;
            @Valid
            @NotNull(message = "Se requiere la presentación del producto de proveedor")
            private Presentacion presentacion;
            private boolean inventariado;

            @Override
            public Producto instance() {
                return Producto.builder()
                        .proveedor(proveedor.instance(Proveedor.class))
                        .nombre(nombre)
                        .precio(precio)
                        .presentacion(presentacion)
                        .inventariado(inventariado)
                        .build();
            }
        }
    }
}