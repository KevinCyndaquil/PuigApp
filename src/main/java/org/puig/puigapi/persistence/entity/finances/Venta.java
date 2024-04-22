package org.puig.puigapi.persistence.entity.finances;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.puig.puigapi.exceptions.VentaInvalidaException;
import org.puig.puigapi.persistence.entity.operation.Usuario;
import org.puig.puigapi.persistence.entity.utils.Articulo;
import org.puig.puigapi.persistence.entity.utils.DetalleDe;
import org.puig.puigapi.persistence.entity.operation.Empleado;
import org.puig.puigapi.persistence.entity.operation.Sucursal;
import org.puig.puigapi.persistence.entity.utils.persistence.Irrepetibe;
import org.puig.puigapi.persistence.entity.utils.persistence.PostEntity;
import org.springframework.beans.factory.annotation.Value;
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

@JsonIgnoreProperties(value = { "target", "source" })
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "forma_entrega"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Venta.class, names = {"PARA_LLEVAR", "RESTAURANTE"}),
        @JsonSubTypes.Type(value = Venta.Reparto.class, name = "REPARTO")
})

@Data
@SuperBuilder
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
    private Set<DetalleDe<Articulo>> detalle = new HashSet<>();
    @JsonProperty(access = Access.READ_ONLY) private double monto_total;
    @JsonProperty(access = Access.READ_ONLY) private double pago_total;
    private Usuario cliente;
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
        update();
        return monto_total < pago_total;
    }

    public void update() {
        this.monto_total = getMonto_total();
        this.pago_total = getPago_total();
    }

    @Data
    @NoArgsConstructor
    public static class Request implements PostEntity<Venta> {
        private Usuario.VentaRequest cliente;
        @NotEmpty(message = "Se deben añadir artículos a la venta_request")
        private Set<DetalleDe<Articulo>> detalle;
        @NotNull(message = "Se requiere la forma de entrega de la venta")
        private FormasEntrega forma_entrega;
        @NotNull(message = "Se require la sucursal donde se está realizando la venta")
        private Sucursal realizada_en;
        @NotNull(message = "Se requiere el empleado que inicio sesión")
        private Empleado tomada_por;
        @NotEmpty(message = "Se deben añadir pagos a la veta")
        private List<Pago> pagos;
        private boolean internet;

        @Override
        public Venta instance() {
            return Venta.builder()
                    .cliente(cliente != null ? cliente.instance() : null)
                    .detalle(detalle)
                    .forma_entrega(forma_entrega)
                    .realizada_en(realizada_en)
                    .tomada_por(tomada_por)
                    .fecha_venta(LocalDateTime.now())
                    .pagos(pagos)
                    .internet(internet)
                    .build();
        }
    }

    /**
     * Venta que se realiza con la finalidad de llevarse hasta la casa o ubicación del cliente.
     */

    @Data
    @SuperBuilder
    @NoArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    @Document(collection = "finances")
    public static class Reparto extends Venta {
        @DBRef private Usuario cliente_reparto;
        private double costo_reparto;
        private Empleado repartidor;

        @JsonSetter("repartidor")
        public void setRepartidor(Empleado repartidor) {
            if (repartidor != null)
                if (repartidor.getPuesto() != Empleado.Puestos.REPARTIDOR)
                    throw VentaInvalidaException.empleadoNoEsRepartidor(repartidor);
            this.repartidor = repartidor;
        }

        @Data
        @NoArgsConstructor
        public static class Request implements PostEntity<Reparto> {
            @NotNull(message = "Se require el cliente de la compra de reparto")
            @Valid private Usuario.RepartoRequest cliente;
            @NotEmpty(message = "Se deben añadir artículos a la venta_request")
            private Set<DetalleDe<Articulo>> detalle;
            @NotNull(message = "Se require la sucursal donde se está realizando la venta")
            private Sucursal realizada_en;
            @NotNull(message = "Se requiere el empleado que inicio sesión")
            private Empleado tomada_por;
            @NotEmpty(message = "Se deben añadir pagos a la venta")
            private List<Pago> pagos;
            private boolean internet = false;
            @PositiveOrZero(message = "Costo de reparto debe ser mayor o igual a cero")
            @Value("${puig.ventas.costo-reparto}")
            private double costo_reparto;

            @Override
            public Reparto instance() {
                return Reparto.builder()
                        .detalle(detalle)
                        .forma_entrega(FormasEntrega.REPARTO)
                        .realizada_en(realizada_en)
                        .fecha_venta(LocalDateTime.now())
                        .tomada_por(tomada_por)
                        .pagos(pagos)
                        .internet(internet)
                        .cliente_reparto(cliente.instance())
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
    @NoArgsConstructor
    public static class ReporteProducto {
        private ArticuloMenu articulo;
        private double cantidad_total;
        private double monto_total;
    }
}