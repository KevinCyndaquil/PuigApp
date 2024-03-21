package org.puig.puigapi.persistence.entity.finances;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.persistence.entity.utils.Direccion;
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
@EqualsAndHashCode(exclude = {"detalle", "monto_total", "forma_entrega", "fecha_venta", "asignada_a", "realizada_en", "pagos"})
@Document(collection = "finances")
public class Venta {
    @Id private String _id;
    private @NotNull Set<Detalle> detalle;
    private float monto_total;
    private @NotNull FormasEntrega forma_entrega;
    private @NotNull LocalDate fecha_venta;
    @DBRef private @NotNull Sucursal realizada_en;
    @DBRef private @NotNull Empleado asignada_a;
    private @NotNull List<Pago> pagos;
    private boolean internet = false;

    public enum FormasEntrega {
        PARA_LLEVAR,
        RESTAURANTE,
        REPARTO,
    }

    @Data
    @NoArgsConstructor
    @EqualsAndHashCode(exclude = {"cantidad", "subtotal"})
    public static class Detalle {
        @DBRef private @NotNull Articulo articulo;
        private int cantidad;
        private float subtotal;
    }

    @Data
    @NoArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    @Document(collection = "finances")
    public static class Reparto extends Venta {
        private @NotNull Direccion direccion;
        private float costo;
        private String nombre_cliente;
        private String telefono_cliente;
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