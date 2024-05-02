package org.puig.puigapi.persistence.entity.finances;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;
import org.puig.puigapi.exceptions.VentaInvalidaException;
import org.puig.puigapi.persistence.entity.admin.Proveedor;
import org.puig.puigapi.persistence.entity.operation.Usuario;
import org.puig.puigapi.util.Articulo;
import org.puig.puigapi.util.contable.Calculable;
import org.puig.puigapi.persistence.entity.operation.Empleado;
import org.puig.puigapi.persistence.entity.operation.Sucursal;
import org.puig.puigapi.util.contable.Contable;
import org.puig.puigapi.util.contable.Detalle;
import org.puig.puigapi.util.data.Direccion;
import org.puig.puigapi.util.persistence.Instantiator;
import org.puig.puigapi.util.persistence.Irrepetibe;
import org.puig.puigapi.util.persistence.SimpleInstance;
import org.puig.puigapi.util.persistence.Updatable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;

/**
 * Conjunto de caracteristicas que permiten el realizar un intercambio.
 * La finalidad de ofrecer un artículo o combo a sus clientes a cambio de obtener ganacia.
 */

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "modo_entrega")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Venta.class, names = {"PARA_LLEVAR", "EN_RESTAURANTE"}),
        @JsonSubTypes.Type(value = Venta.Reparto.class, name = "REPARTO")})

@Data
@SuperBuilder
@NoArgsConstructor
@Document(collection = "finances")
public class Venta implements Irrepetibe<String>, Updatable {
    @Id private String id;
    private Ticket ticket;
    private Usuario cliente;
    private ModosDeEntrega modo_entrega;
    private LocalDateTime fecha_venta;
    @DBRef(lazy = true) private Sucursal realizada_en;
    @DBRef private Empleado tomada_por;
    @DBRef private Empleado asignada_a;
    private List<Pago> pagos;
    private boolean internet;

    @JsonProperty(access = Access.READ_ONLY) private double monto_total;
    @JsonProperty(access = Access.READ_ONLY) private double pago_total;

    public enum ModosDeEntrega {
        PARA_LLEVAR,
        EN_RESTAURANTE,
        REPARTO,
    }

    public void forEachDetalle(@NonNull Consumer<Contable<Proveedor.Producto>> consumer) {
        this.getTicket().forEach(t -> t.getDetalle()
                .getContenido()
                .per(t.getCantidad())
                .forEach(consumer));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Venta venta)
            return id.equals(venta.id);
        return false;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @JsonGetter("monto_total")
    public double getMonto_total() {
        return ticket.stream()
                .map(Calculable::getMonto)
                .reduce(0d, Double::sum);
    }

    @JsonGetter("pago_total")
    public double getPago_total() {
        return pagos.stream()
                .map(Pago::getPago)
                .reduce(0d, Double::sum);
    }

    public void validar() {
        if (getMonto_total() > getPago_total())
            throw VentaInvalidaException.pagoInferiorAMonto(this);
    }

    @Override
    public void update() {
        this.monto_total = getMonto_total();
        this.pago_total = getPago_total();
    }

    @Data
    @NoArgsConstructor
    public static class PostRequest implements Instantiator<Venta> {
        @NotEmpty(message = "Se deben añadir artículos a la venta_request")
        private Venta.Ticket ticket;
        private Usuario.FacturableRequest cliente;
        @NotNull(message = "Se requiere la forma de entrega de la venta")
        private Venta.ModosDeEntrega modo_entrega;
        @NotNull(message = "Se require la sucursal donde se está realizando la venta")
        private SimpleInstance<String> realizada_en;
        @NotNull(message = "Se requiere el empleado que inicio sesión")
        private SimpleInstance<ObjectId> tomada_por;
        @NotEmpty(message = "Se deben añadir pagos a la veta")
        private List<Pago> pagos;
        private boolean internet = false;

        @Override
        public Venta instance() {
            return Venta.builder()
                    .ticket(ticket)
                    .cliente(Instantiator.nullOrGet(cliente))
                    .modo_entrega(modo_entrega)
                    .realizada_en(realizada_en.instance(Sucursal.class))
                    .tomada_por(tomada_por.instance(Empleado.class))
                    .fecha_venta(LocalDateTime.now())
                    .pagos(pagos)
                    .internet(internet)
                    .build();
        }
    }

    /**
     * Venta que se realiza con la finalidad de llevarse hasta la casa o ubicación del cliente_reparto.
     */

    @Data
    @SuperBuilder
    @NoArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    @Document(collection = "finances")
    public static class Reparto extends Venta {
        @DBRef private Usuario cliente_reparto;
        private Direccion direccion_reparto;
        private double costo_reparto;
        @DBRef private Empleado repartidor;

        @JsonSetter("repartidor")
        public void setRepartidor(@NonNull Empleado repartidor) {
            if (repartidor.getPuesto() != Empleado.Puestos.REPARTIDOR)
                throw VentaInvalidaException.empleadoNoEsRepartidor(repartidor);
            this.repartidor = repartidor;
        }

        @Override
        public double getMonto_total() {
            return super.getMonto_total() + costo_reparto;
        }

        @Data
        @NoArgsConstructor
        public static class PostRequest implements Instantiator<Reparto> {
            @NotEmpty(message = "Se deben añadir artículos a la venta")
            private Venta.Ticket ticket;
            private Usuario.FacturableRequest cliente;
            @NotNull(message = "Se require la sucursal donde se está realizando la venta")
            private SimpleInstance<String> realizada_en;
            @NotNull(message = "Se requiere el empleado que inicio sesión")
            private SimpleInstance<ObjectId> tomada_por;
            @NotEmpty(message = "Se deben añadir pagos a la venta")
            private List<Pago> pagos;
            private boolean internet = false;
            @Valid
            @NotNull(message = "Se require el cliente reparto de la compra de reparto")
            private Usuario.ClienteRepartoRequest cliente_reparto;
            @Valid
            @NotNull(message = "Se requiere la ubicación del reparto")
            private Direccion direccion_reparto;
            @Value("${puig.ventas.costo-reparto}")
            @PositiveOrZero(message = "Costo de reparto debe ser mayor o igual a cero")
            private double costo_reparto = 30;

            @Override
            public Reparto instance() {
                return Reparto.builder()
                        .ticket(ticket)
                        .cliente(Instantiator.nullOrGet(cliente))
                        .modo_entrega(ModosDeEntrega.REPARTO)
                        .realizada_en(realizada_en.instance(Sucursal.class))
                        .tomada_por(tomada_por.instance(Empleado.class))
                        .fecha_venta(LocalDateTime.now())
                        .pagos(pagos)
                        .internet(internet)
                        .cliente_reparto(cliente_reparto.instance())
                        .direccion_reparto(direccion_reparto)
                        .costo_reparto(costo_reparto)
                        .build();
            }
        }
    }

    @Data
    @NoArgsConstructor
    public static class Pago {
        private double pago;
        @NotNull(message = "Se requiere ingresar el modo de pago")
        private Modo modo = Modo.EFECTIVO;

        public enum Modo {
            EFECTIVO,
            DEBITO,
            CREDITO
        }
    }

    @Data
    public static class Ticket extends Detalle<Calculable<Articulo>> {

    }

    @Data
    @NoArgsConstructor
    public static class ReporteProducto {
        private ArticuloMenu articulo;
        private double cantidad_total;
        private double monto_total;
    }
}