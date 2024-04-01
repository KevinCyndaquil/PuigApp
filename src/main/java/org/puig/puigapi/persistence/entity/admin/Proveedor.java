package org.puig.puigapi.persistence.entity.admin;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.errors.PrecioInvalidoException;
import org.puig.puigapi.persistence.entity.utils.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.HashSet;
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
    @NotNull private String nombre;
    private String telefono_fijo;
    private String telefono_movil;
    @NotNull private String rfc;
    private String correo;
    private Direccion ubicacion;
    @NotNull private RazonesSociales razon = RazonesSociales.MORAL;
    @NotNull private Set<Tarjeta> cuentas = new HashSet<>();

    @DBRef
    @JsonIgnore
    private Set<Producto> productos;

    public enum RazonesSociales {
        FISICO,
        MORAL
    }

    /**
     * Es un comprobante oficial o no que contiene un detalle de la materia proporcionada así cómo
     * quién la proporciono y su costo.
     */
    @Data
    @NoArgsConstructor
    @EqualsAndHashCode(exclude = {"detalle", "recepcion", "monto", "iva", "monto_total"})
    @Document(collection = "admin")
    public static class Factura implements Irrepetibe<String> {
        @NotNull @Id private String folio;
        @NotNull private Proveedor proveedor;
        @NotNull private Set<Detalle<Producto>> detalle = new HashSet<>();
        @NotNull private LocalDate recepcion;
        private double monto;
        private double iva;
        private double monto_total;

        @Builder
        @JsonCreator
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
    @NoArgsConstructor
    @EqualsAndHashCode(exclude = {"proveedor", "nombre", "precio", "presentacion"})
    @Document(collection = "admin")
    public static class Producto
            implements Irrepetibe<String>, ObjetoConPrecio {

        @Id private String id;
        @DBRef private Proveedor proveedor;
        private @NotNull String nombre;
        private double precio;
        private @NotNull Presentacion presentacion;

        @Builder
        @JsonCreator
        public Producto(String id,
                        Proveedor proveedor,
                        @NotNull String nombre,
                        double precio,
                        @NotNull Presentacion presentacion) {
            this.id = id;
            this.proveedor = proveedor;
            this.nombre = nombre;
            if (precio <= 0d) throw new PrecioInvalidoException(getClass());
            this.precio = precio;
            this.presentacion = presentacion;
        }
    }
}