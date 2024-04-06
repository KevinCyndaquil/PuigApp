package org.puig.puigapi.persistence.entity.finances;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.errors.CreationVentaException;
import org.puig.puigapi.errors.EmptyCollectionException;
import org.puig.puigapi.persistence.entity.utils.Detalle;
import org.puig.puigapi.persistence.entity.utils.Direccion;
import org.puig.puigapi.persistence.entity.operation.Empleado;
import org.puig.puigapi.persistence.entity.operation.Sucursal;
import org.puig.puigapi.persistence.entity.utils.Irrepetibe;
import org.puig.puigapi.persistence.entity.utils.PostEntity;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@SuperBuilder
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
    @Id protected String id;
    @NotNull protected Set<Detalle<Articulo>> detalle = new HashSet<>();
    @JsonProperty(access = Access.READ_ONLY) protected double monto_total;
    @JsonProperty(access = Access.READ_ONLY) protected double pago_total;
    @NotNull protected FormasEntrega forma_entrega;
    @NotNull protected LocalDate fecha_venta;
    @NotNull @DBRef protected Sucursal realizada_en;
    @NotNull @DBRef protected Empleado asignada_a;
    @NotNull protected List<Pago> pagos = new ArrayList<>();
    protected boolean internet = false;

    public enum FormasEntrega {
        PARA_LLEVAR,
        RESTAURANTE,
        REPARTO,
    }

    public Venta(@NotNull Set<Detalle<Articulo>> detalle,
                 @NotNull FormasEntrega forma_entrega,
                 @NotNull LocalDate fecha_venta,
                 @NotNull Sucursal realizada_en,
                 @NotNull Empleado asignada_a,
                 @NotNull List<Pago> pagos,
                 boolean internet) {

        if (detalle.isEmpty()) throw new EmptyCollectionException(Venta.class, "detalle");

        this.detalle = detalle;
        this.pagos = pagos;
        definirMontos();

        this.forma_entrega = forma_entrega;
        this.fecha_venta = fecha_venta;
        this.realizada_en = realizada_en;
        this.asignada_a = asignada_a;

        this.internet = internet;
    }

    protected void definirMontos() {
        this.monto_total += detalle.stream()
                .map(Detalle::getMonto)
                .reduce(0d, Double::sum);
        this.pago_total += pagos.stream()
                .map(Pago::getPago)
                .reduce(0d, Double::sum);

        if (pago_total < monto_total)
            throw CreationVentaException.pagoNoAlcanzaMonto(monto_total, pago_total);
    }

    public record Post(@NotNull Set<Detalle<Articulo>> detalle,
                       @NotNull FormasEntrega forma_entrega,
                       @NotNull LocalDate fecha_venta,
                       @NotNull Sucursal realizada_en,
                       @NotNull Empleado asignada_a,
                       @NotNull List<Pago> pagos,
                       boolean internet) implements PostEntity<Venta> {

        @Override
        public Venta instance() {
            return Venta.builder()
                    .detalle(detalle)
                    .forma_entrega(forma_entrega)
                    .fecha_venta(fecha_venta)
                    .realizada_en(realizada_en)
                    .asignada_a(asignada_a)
                    .pagos(pagos)
                    .build();
        }
    }

    @Data
    @SuperBuilder
    @NoArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    @Document(collection = "finances")
    public static class Reparto extends Venta {
        @NotNull private Direccion direccion;
        private double costo_reparto;
        @NotNull private String nombre_cliente;
        @NotNull private String telefono_cliente;

        public Reparto(String id,
                       @NotNull Set<Detalle<Articulo>> detalle,
                       @NotNull FormasEntrega forma_entrega,
                       @NotNull LocalDate fecha_venta,
                       @NotNull Sucursal realizada_en,
                       @NotNull Empleado asignada_a,
                       @NotNull List<Pago> pagos,
                       boolean internet,
                       @NotNull Direccion direccion,
                       double costo_reparto,
                       @NotNull String nombre_cliente,
                       @NotNull String telefono_cliente) {

            if (detalle.isEmpty())
                throw new EmptyCollectionException(Venta.Reparto.class, "detalle");

            this.id = id;

            this.detalle = detalle;
            this.pagos = pagos;
            this.costo_reparto = costo_reparto;
            definirMontos();

            this.forma_entrega = forma_entrega;
            this.fecha_venta = fecha_venta;
            this.realizada_en = realizada_en;
            this.asignada_a = asignada_a;

            this.internet = internet;
            this.direccion = direccion;
            this.nombre_cliente = nombre_cliente;
            this.telefono_cliente = telefono_cliente;
        }

        @Override
        protected void definirMontos() {
            monto_total += costo_reparto;
            super.definirMontos();
        }

        public record Post(Venta.Post post_venta,
                           @NotNull Direccion direccion,
                           double costo_reparto,
                           @NotNull String nombre_cliente,
                           @NotNull String telefono_cliente)
                implements PostEntity<Reparto> {

            @Override
            public Venta.Reparto instance() {
                return Reparto.builder()
                        .detalle(post_venta.detalle)
                        .forma_entrega(post_venta.forma_entrega)
                        .fecha_venta(post_venta.fecha_venta)
                        .realizada_en(post_venta.realizada_en)
                        .asignada_a(post_venta.asignada_a)
                        .pagos(post_venta.pagos)
                        .direccion(direccion)
                        .costo_reparto(costo_reparto)
                        .nombre_cliente(nombre_cliente)
                        .telefono_cliente(telefono_cliente)
                        .build();
            }
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
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