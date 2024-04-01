package org.puig.puigapi.persistence.entity.finances;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.persistence.entity.utils.Detalle;
import org.puig.puigapi.persistence.entity.utils.Direccion;
import org.puig.puigapi.persistence.entity.operation.Empleado;
import org.puig.puigapi.persistence.entity.operation.Sucursal;
import org.puig.puigapi.persistence.entity.utils.Irrepetibe;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {
        "detalle",
        "monto_total",
        "forma_entrega",
        "fecha_venta",
        "asignada_a",
        "realizada_en",
        "pagos",
        "internet"})
@Document(collection = "finances")
public class Venta implements Irrepetibe<String> {
    @Id @JsonProperty(access = Access.READ_ONLY) private String id;
    @NotNull private Set<Detalle<Articulo>> detalle = new HashSet<>();
    @JsonProperty(access = Access.READ_ONLY) private double monto_total;
    @NotNull private FormasEntrega forma_entrega;
    @NotNull private LocalDate fecha_venta;
    @NotNull @DBRef private Sucursal realizada_en;
    @NotNull @DBRef private Empleado asignada_a;
    @NotNull private List<Pago> pagos = new ArrayList<>();
    private boolean internet = false;

    public enum FormasEntrega {
        PARA_LLEVAR,
        RESTAURANTE,
        REPARTO,
    }

    @JsonSetter
    private void setMonto_total() {
        this.monto_total = detalle.stream()
                .map(Detalle::getMonto)
                .reduce(0d, Double::sum);
    }

    @Data
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    @Document(collection = "finances")
    public static class Reparto extends Venta {
        @NotNull private Direccion direccion;
        private double costo_reparto;
        @NotNull private String nombre_cliente;
        @NotNull private String telefono_cliente;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(exclude = {"pago", "modo"})
    public static class Pago {
        private float pago;
        @NotNull private Modo modo;

        public enum Modo {
            EFECTIVO,
            DEBITO,
            CREDITO
        }
    }
}