package org.puig.puigapi.persistence.entity.admin;

import lombok.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.persistence.entity.utils.Direccion;
import org.puig.puigapi.persistence.entity.utils.Presentacion;
import org.puig.puigapi.persistence.entity.utils.Tarjeta;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"nombre", "telefono_fijo", "telefono_movil", "rfc", "ubicacion", "correo", "razon", "cuentas"})
@Document(collection = "admin")
public class Proveedor{
    @Id private String _id;
    private @NotNull String nombre;
    private String telefono_fijo;
    private String telefono_movil;
    private @NotNull String rfc;
    private String correo;
    private Direccion ubicacion;
    private @NotNull Proveedor.RazonesSociales razon = RazonesSociales.MORAL;
    private @NotNull Set<Tarjeta> cuentas;

    public enum RazonesSociales {
        FISICO,
        MORAL
    }

    @Data
    @NoArgsConstructor
    @EqualsAndHashCode(exclude = {"detalle", "recepcion", "monto", "iva", "monto_total"})
    @Document(collection = "admin")
    public static class Factura {
        @Id private @NotNull String _folio;
        private @NotNull Proveedor proveedor;
        @DBRef private @NotNull Set<Detalle> detalle;
        private @NotNull LocalDate recepcion;
        private float monto;
        private float iva;
        private float monto_total;

        public Factura(@NotNull String _folio,
                       @NotNull Proveedor proveedor,
                       @NotNull Set<Detalle> detalle,
                       @NotNull LocalDate recepcion,
                       float iva) {
            this._folio = _folio;
            this.proveedor = proveedor;
            this.recepcion = recepcion;
            this.monto = detalle.stream()
                    .map(Detalle::getMonto)
                    .reduce(0f, Float::sum);
            this.iva = iva;
            this.monto_total = monto + iva;
        }


        @Data
        @NoArgsConstructor
        @EqualsAndHashCode(exclude = {"cantidad", "monto"})
        public static class Detalle {
            @DBRef private @NotNull Producto producto;
            private int cantidad;
            private float monto;

            @Contract(pure = true)
            public Detalle(@NotNull Producto producto, int cantidad) {
                this.producto = producto;
                this.cantidad = cantidad;
                this.monto = producto.getPrecio() * cantidad;
            }
        }
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(exclude = {"proveedor", "nombre", "precio", "presentacion"})
    @Document(collection = "admin")
    public static class Producto {
        @Id private String _codigo;
        @DBRef private Proveedor proveedor;
        private @NotNull String nombre;
        private float precio;
        private @NotNull Presentacion presentacion;
    }
}
