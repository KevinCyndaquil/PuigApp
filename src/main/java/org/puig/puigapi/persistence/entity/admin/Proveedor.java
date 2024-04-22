package org.puig.puigapi.persistence.entity.admin;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.annotation.JsonSetter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import org.puig.puigapi.persistence.entity.operation.Sucursal;
import org.puig.puigapi.persistence.entity.utils.*;
import org.puig.puigapi.persistence.entity.utils.data.Correo;
import org.puig.puigapi.persistence.entity.utils.data.RFC;
import org.puig.puigapi.persistence.entity.utils.data.Telefono;
import org.puig.puigapi.persistence.entity.utils.persistence.Irrepetibe;
import org.puig.puigapi.persistence.entity.utils.persistence.PostEntity;
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
public class Proveedor implements Irrepetibe<String> {
    @Id private String id;
    private String nombre;
    private Telefono telefono_fijo;
    private Telefono telefono_movil;
    private RFC rfc;
    private Correo correo;
    private Direccion ubicacion;
    private RazonesSociales razon;
    private Set<Tarjeta> cuentas = new HashSet<>();

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

    @Data
    @NoArgsConstructor
    public static class Request implements PostEntity<Proveedor> {
        @NotBlank(message = "Se requiere un nombre para el proveedor")
        @Pattern(regexp = "(?U)^[\\p{Lu}\\p{M}\\d]+( [\\p{Lu}\\p{M}\\d]+)*$",
                message = "Formato para nombres invalido")
        private String nombre;
        @NotNull(message = "Se requiere un número de telefono fijo")
        @Valid
        private Telefono telefono_fijo;
        @Valid private Telefono telefono_movil;
        @NotNull(message = "Se requiere un rfc para el proveedor")
        @Valid
        private RFC rfc;
        @Valid private Correo correo;
        @Valid private Direccion.RequestFacturacion ubicacion;
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
                            .map(PostEntity::instance)
                            .collect(Collectors.toSet()))
                    .build();
        }
    }

    /**
     * Es un comprobante oficial o no que contiene un detalle de la materia proporcionada así cómo
     * quién la proporciono y su costo.
     */

    @JsonIgnoreProperties(value = { "target", "source" })

    @Data
    @NoArgsConstructor
    @EqualsAndHashCode(exclude = {
            "proveedor",
            "detalle",
            "recepcion",
            "monto",
            "total_iva",
            "monto_total"})
    @Document(collection = "admin")
    public static class Factura implements Irrepetibe<String> {
        @Id private String folio;
        @DBRef private Proveedor proveedor;
        private Set<Detalle> detalle = new HashSet<>();
        private LocalDateTime recepcion = LocalDateTime.now();
        @DBRef private Sucursal sucursal;
        @JsonProperty(access = Access.READ_ONLY) private double monto;
        @JsonProperty(access = Access.READ_ONLY) private double monto_total;
        @JsonProperty(access = Access.READ_ONLY) private double total_iva;

        @Builder
        public Factura(String folio,
                       Proveedor proveedor,
                       Set<Detalle> detalle,
                       Sucursal sucursal) {
            this.folio = folio;
            this.proveedor = proveedor;
            this.detalle = detalle;
            this.sucursal = sucursal;
        }

        @Override
        public String getId() {
            return folio;
        }

        @JsonSetter("detalle")
        public void setDetalle(Set<Detalle> detalle) {
            this.detalle = detalle;
            update();
        }

        @JsonGetter("total_iva")
        public double getTotal_iva() {
            return detalle.stream()
                    .map(Detalle::getIva)
                    .reduce(0d, Double::sum);
        }

        @JsonGetter("monto")
        public double getMonto() {
            return getMonto_total() - getTotal_iva();
        }

        @JsonGetter("monto_total")
        public double getMonto_total() {
            return detalle.stream()
                    .map(Detalle::getMonto)
                    .reduce(0d, Double::sum);

        }

        public void update() {
            this.monto = getMonto();
            this.total_iva = getTotal_iva();
            this.monto_total = getMonto_total();
        }

        @Data
        @NoArgsConstructor
        @EqualsAndHashCode(callSuper = true)
        public static class Detalle extends DetalleDe<Producto> {
            @PositiveOrZero(message = "Iva calculado para el producto incluído en la factura de proveedor debe ser mayor o igual a cero")
            private double iva;

            @Override
            public double getMonto() {
                final double cantidadPresentacion = objeto.getPresentacion() == null ?
                        1 :
                        objeto.getPresentacion().getCantidad();
                final double cantidad = super.cantidad / cantidadPresentacion;

                return cantidad * objeto.getPrecio() + iva;
            }
        }

        @Data
        @NoArgsConstructor
        public static class Request implements PostEntity<Factura> {
            @NotBlank(message = "Folio de factura no puede estar vacío")
            private String folio;
            @NotNull(message = "Se requiere el id del proveedor de la factura")
            private Proveedor proveedor;
            @NotEmpty(message = "Se deben ingresar productos a una factura para su creación")
            private Set<Detalle> detalle;
            @NotNull(message = "Se requiere la sucursal en la que se recibió la factura")
            private Sucursal sucursal;

            @Override
            public Factura instance() {
                return Factura.builder()
                        .folio(folio)
                        .proveedor(proveedor)
                        .detalle(detalle)
                        .sucursal(sucursal)
                        .build();
            }
        }
    }

    /**
     * Productos sumistrados y detallados por un proveedor.
     */

    @JsonIgnoreProperties(value = {"target", "source"})

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Document(collection = "admin")
    public static class Producto
            implements Irrepetibe<String>, ObjetoConPrecio {
        @Id private String id;
        @DBRef(lazy = true) private Proveedor proveedor;
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

        @Data
        @NoArgsConstructor
        public static class Request implements PostEntity<Producto> {
            @NotNull(message = "Se requiere el id del proveedor del producto")
            private Proveedor proveedor;
            @NotBlank(message = "Se require un nombre para el producto de proveedor")
            private String nombre;
            @Positive(message = "El precio debe ser mayor a cero")
            private double precio;
            @NotNull(message = "Se requiere la presentación del producto de proveedor")
            @Valid
            private Presentacion.Request presentacion;

            @Override
            public Producto instance() {
                return Producto.builder()
                        .proveedor(proveedor)
                        .nombre(nombre)
                        .precio(precio)
                        .presentacion(presentacion.instance())
                        .build();
            }
        }
    }
}