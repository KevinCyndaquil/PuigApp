package org.puig.puigapi.persistence.entity.finances;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.persistence.entity.utils.DetalleDe;
import org.puig.puigapi.persistence.entity.utils.Direccion;
import org.puig.puigapi.persistence.entity.operation.Empleado;
import org.puig.puigapi.persistence.entity.operation.Sucursal;
import org.puig.puigapi.persistence.entity.utils.persistence.Irrepetibe;
import org.puig.puigapi.persistence.entity.utils.persistence.PostEntity;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Conjunto de caracteristicas que permiten el realizar un intercambio.
 * La finalidad de ofrecer un artículo o combo a sus clientes a cambio de obtener ganacia.
 */

@JsonIgnoreProperties(value = { "target" })

@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {
        "detalle",
        "monto_total",
        "pago_total",
        "forma_entrega",
        "fecha_venta",
        "asignada_a",
        "realizada_en",
        "pagos",
        "internet"})
@Document(collection = "finances")
public class Venta implements Irrepetibe<String> {
    @Id private String id;
    @DBRef private Set<DetalleDe<Articulo>> detalle = new HashSet<>();
    @JsonProperty(access = Access.READ_ONLY) private double monto_total;
    @JsonProperty(access = Access.READ_ONLY) private double pago_total;
    private FormasEntrega forma_entrega;
    private LocalDateTime fecha_venta;
    @DBRef private Sucursal realizada_en;
    @DBRef private Empleado tomada_por;
    @DBRef private Empleado asignada_a;
    private List<Pago> pagos = new ArrayList<>();
    private boolean internet = false;

    public enum FormasEntrega {
        PARA_LLEVAR,
        RESTAURANTE,
        REPARTO,
    }

    @JsonGetter("monto_total")
    public double getMonto_total() {
        return detalle.stream()
                .map(DetalleDe::getMonto)
                .reduce(0d, Double::sum);
    }

    @JsonGetter("pago_total")
    public double getPago_total() {
        return pagos.stream()
                .map(Pago::getPago)
                .reduce(0d, Double::sum);
    }

    public boolean esValida() {
        return getMonto_total() < getPago_total();
    }

    @Builder
    public Venta(@NotNull Set<DetalleDe<Articulo>> detalle,
                 @NotNull FormasEntrega forma_entrega,
                 @NotNull Sucursal realizada_en,
                 @NotNull Empleado tomada_por,
                 @NotNull List<Pago> pagos,
                 boolean internet) {
        this.detalle = detalle;
        this.pagos = pagos;

        this.forma_entrega = forma_entrega;
        this.fecha_venta = LocalDateTime.now();
        this.realizada_en = realizada_en;
        this.tomada_por = tomada_por;

        this.internet = internet;
    }

    @Data
    @NoArgsConstructor
    public static class Request implements PostEntity<Venta> {
        @NotEmpty(message = "Se deben añadir artículos a la venta_request")
        private Set<DetalleDe<Articulo>> detalle;
        @NotNull private FormasEntrega forma_entrega;
        @NotNull private Sucursal realizada_en;
        @NotNull private Empleado tomada_por;
        @NotEmpty(message = "Se deben añadir pagos a la veta")
        private List<Pago> pagos;
        private boolean internet;

        @Override
        public Venta instance() {
            return Venta.builder()
                    .detalle(detalle)
                    .forma_entrega(forma_entrega)
                    .realizada_en(realizada_en)
                    .tomada_por(tomada_por)
                    .pagos(pagos)
                    .build();
        }
    }

    /**
     * Venta que se realiza con la finalidad de llevarse hasta la casa o ubicación del cliente.
     */

    @Data
    @NoArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    @Document(collection = "finances")
    public static class Reparto extends Venta {
        private Direccion direccion;
        private double costo_reparto;
        private String nombre_cliente;
        private String telefono_cliente;
        private Empleado repartidor;

        @Builder(builderMethodName = "buildReparto")
        public Reparto(@NotEmpty Set<DetalleDe<Articulo>> detalle,
                       @NotNull FormasEntrega forma_entrega,
                       @NotNull Sucursal realizada_en,
                       @NotNull Empleado tomada_por,
                       @NotEmpty List<Pago> pagos,
                       boolean internet,
                       @NotNull Direccion direccion,
                       double costo_reparto,
                       @NotBlank String nombre_cliente,
                       @NotBlank String telefono_cliente,
                       @NotNull Empleado repartidor) {
            super(detalle, forma_entrega, realizada_en, tomada_por, pagos, internet);
            this.direccion = direccion;
            this.costo_reparto = costo_reparto;
            this.nombre_cliente = nombre_cliente;
            this.telefono_cliente = telefono_cliente;
            if (repartidor.getPuesto() != Empleado.Puestos.REPARTIDOR)
                throw new IllegalArgumentException(
                        "Repartidor asignado a la venta_request no es un empleado contratado como repartidor");
            this.repartidor = repartidor;
        }

        @Data
        @NoArgsConstructor
        public static class Request implements PostEntity<Reparto> {
            @NotNull private Venta.Request venta_request;
            @NotNull private Direccion.Request direccion;
            private double costo_reparto;
            @Pattern(regexp = "^[A-Z]+(?: [A-Z]+)*$",
                    message = "Nombre de cliente de venta de reparto invalido. Recuerda que debe ir en mayúsculas")
            private String nombre_cliente;
            @Pattern(regexp = "^\\+\\([0-9]{2}\\) [0-9]{3} [0-9]{3} [0-9]{4}$",
                    message = "Teléfono móvil de venta de reparto no es válido")
            private String telefono_cliente;
            @NotNull private Empleado repartidor;

            @Override
            public Reparto instance() {
                return Reparto.buildReparto()
                        .detalle(venta_request.detalle)
                        .forma_entrega(venta_request.forma_entrega)
                        .realizada_en(venta_request.realizada_en)
                        .tomada_por(venta_request.tomada_por)
                        .pagos(venta_request.pagos)
                        .direccion(direccion.instance())
                        .costo_reparto(costo_reparto)
                        .nombre_cliente(nombre_cliente)
                        .telefono_cliente(telefono_cliente)
                        .repartidor(repartidor)
                        .build();
            }
        }
    }

    @Data
    @NoArgsConstructor
    @EqualsAndHashCode(exclude = {"pago", "modo"})
    public static class Pago {
        private double pago;
        @NotNull private Modo modo;

        public enum Modo {
            EFECTIVO,
            DEBITO,
            CREDITO
        }
    }
}