package org.puig.puigapi.persistence.entity.admin;

import lombok.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.persistence.entity.Direccion;
import org.puig.puigapi.persistence.entity.IPersona;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "admin")
public class Proveedor extends IPersona {
    private Direccion ubicacion;
    private String correo;

    public Proveedor(String _id,
                     @NotNull String nombre,
                     String telefono,
                     @NotNull String rfc,
                     Direccion ubicacion,
                     String correo) {
        super(_id, nombre, telefono, rfc);
        this.ubicacion = ubicacion;
        this.correo = correo;
    }

    @Data
    @NoArgsConstructor
    @EqualsAndHashCode(exclude = {"descripcion", "recepcion", "precio", "iva", "monto_total"})
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
        @EqualsAndHashCode(exclude = {"cantidad", "precio"})
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
