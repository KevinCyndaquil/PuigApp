package org.puig.puigapi.persistence.entity.finances;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.persistence.entity.Direccion;
import org.puig.puigapi.persistence.entity.IMenu;
import org.puig.puigapi.persistence.entity.operation.Empleado;
import org.puig.puigapi.persistence.entity.operation.Sucursal;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "finances")
public class Venta {
    @Id private String _id;
    private @NotNull Set<Detalle> productos;
    private float monto_total;
    private @NotNull FormasEntrega forma_entrega;
    private @NotNull LocalDate fecha_venta;
    @DBRef private @NotNull Empleado tomo_orden;
    @DBRef private @NotNull Sucursal tomada_en;
    private @NotNull List<Pago> pagos;

    public Venta(String _id,
                 @NotNull Set<Detalle> productos,
                 @NotNull FormasEntrega forma_entrega,
                 @NotNull Empleado tomo_orden,
                 @NotNull Sucursal tomada_en,
                 @NotNull List<Pago> pagos) {
        this._id = _id;
        this.productos = productos;
        this.forma_entrega = forma_entrega;
        this.tomo_orden = tomo_orden;
        this.tomada_en = tomada_en;
        this.pagos = pagos;
        this.monto_total = pagos.stream()
                .map(Pago::getPago)
                .reduce(0f, Float::sum);
    }

    public enum FormasEntrega {
        PARA_LLEVAR,
        RESTAURANTE,
        REPARTO,
    }

    @Data
    @NoArgsConstructor
    @EqualsAndHashCode(exclude = {"cantidad", "subtotal"})
    public static class Detalle {
        @DBRef private @NotNull IMenu producto;
        private int cantidad;
        private float subtotal;

        public Detalle(@NotNull IMenu producto, int cantidad) {
            this.producto = producto;
            this.cantidad = cantidad;
            this.subtotal = producto.getPrecio() * cantidad;
        }
    }

    @Data
    @NoArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    @Document(collection = "finances")
    public static class Reparto extends Venta {
        private @NotNull Direccion direccion;
        private String informacion;
        private float costo;

        public Reparto(String _id,
                       @NotNull Set<Detalle> productos,
                       @NotNull FormasEntrega formaEntrega,
                       @NotNull Empleado tomo_orden,
                       @NotNull Sucursal tomada_en,
                       @NotNull List<Pago> pagos,
                       @NotNull Direccion direccion,
                       String informacion,
                       float costo) {
            super(_id, productos, formaEntrega, tomo_orden, tomada_en, pagos);
            this.direccion = direccion;
            this.informacion = informacion;
            this.costo = costo;
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(exclude = {"pago", "modo"})
    public static class Pago {
        private float pago;
        private @NotNull Modo modo;

        public enum Modo {
            EFECTIVO,
            DEBITO,
            CREDITO
        }
    }
}