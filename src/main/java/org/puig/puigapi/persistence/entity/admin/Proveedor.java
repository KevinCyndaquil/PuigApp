package org.puig.puigapi.persistence.entity.admin;

import lombok.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.persistence.entity.DatoBanco;
import org.puig.puigapi.persistence.entity.Direccion;
import org.puig.puigapi.persistence.entity.Social;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false, exclude = {
        "nombre",
        "telefono_movil",
        "ubicacion",
        "correo",
        "tipo"})
@Document(collection = "admin")
public class Proveedor extends Social {
    @Id private String _id;
    private @NotNull String nombre;
    private String telefono_movil;
    private Direccion ubicacion;
    private String correo;
    private @NotNull Tipo tipo = Tipo.MORAL;

    public enum Tipo {
        FISICO,
        MORAL
    }

    public Proveedor(String telefono,
                     @NotNull String rfc,
                     DatoBanco datos_bancarios,
                     @NotNull String nombre,
                     String telefono_movil,
                     Direccion ubicacion,
                     String correo,
                     @NotNull Tipo tipo) {
        super(telefono, rfc, datos_bancarios);
        this.nombre = nombre;
        this.telefono_movil = telefono_movil;
        this.ubicacion = ubicacion;
        this.correo = correo;
        this.tipo = tipo;
    }

    @Data
    @NoArgsConstructor
    @EqualsAndHashCode(exclude = {"descripcion", "recepcion", "monto", "iva", "monto_total"})
    @Document(collection = "admin")
    public static class Factura {
        @Id private @NotNull String _folio;
        private @NotNull Proveedor proveedor;
        @DBRef private @NotNull Set<Descripcion> descripcion;
        private @NotNull LocalDate recepcion;
        private float monto;
        private float iva;
        private float monto_total;

        public Factura(@NotNull String _folio,
                       @NotNull Proveedor proveedor,
                       @NotNull Set<Descripcion> descripcion,
                       @NotNull LocalDate recepcion,
                       float iva) {
            this._folio = _folio;
            this.proveedor = proveedor;
            this.recepcion = recepcion;
            this.monto = descripcion.stream()
                    .map(Descripcion::getMonto)
                    .reduce(0f, Float::sum);
            this.iva = iva;
            this.monto_total = monto + iva;
        }


        @Data
        @NoArgsConstructor
        @EqualsAndHashCode(exclude = {"cantidad", "monto"})
        public static class Descripcion {
            @DBRef private @NotNull Producto producto;
            private int cantidad;
            private float monto;

            @Contract(pure = true)
            public Descripcion(@NotNull Producto producto, int cantidad) {
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
        @Id private String _id;
        @DBRef private Proveedor proveedor;
        private @NotNull String nombre;
        private float precio;
        private @NotNull Presentacion presentacion;
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Presentacion {
        private float peso;
        private @NotNull Tipos tipo;

        public Presentacion(float peso) {
            this.peso = peso;
            this.tipo = Tipos.PESO;
        }

        public enum Tipos {
            PESO,
            CAJA,
            BOLSA,
            CUBETA,
            PIEZA
        }
    }
}
