package org.puig.puigapi.persistence.entity.finances;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.annotation.JsonSetter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.puig.puigapi.exceptions.VentaInvalidaException;
import org.puig.puigapi.persistence.entity.utils.Articulo;
import org.puig.puigapi.persistence.entity.utils.DetalleDe;
import org.puig.puigapi.persistence.entity.utils.Direccion;
import org.puig.puigapi.persistence.entity.operation.Empleado;
import org.puig.puigapi.persistence.entity.operation.Sucursal;
import org.puig.puigapi.persistence.entity.utils.data.Telefono;
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

    public void update() {
        this.monto_total = getMonto_total();
        this.pago_total = getPago_total();
    }

    @Data
    @NoArgsConstructor
    public static class Request implements PostEntity<Venta> {
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
                    .detalle(detalle)
                    .forma_entrega(forma_entrega)
                    .realizada_en(realizada_en)
                    .tomada_por(tomada_por)
                    .fecha_venta(LocalDateTime.now())
                    .pagos(pagos)
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
        private Direccion direccion;
        private double costo_reparto;
        private String nombre_cliente;
        private Telefono telefono_cliente;
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
            @Valid
            @NotNull(message = "Se requiere una instancia correcta de Venta.Request")
            private Venta.Request venta_request;
            @Valid
            @NotNull(message = "Se requiere una direccion de entrega para el reparto")
            private Direccion.RequestUsuario direccion;
            @PositiveOrZero(message = "Costo de reparto debe ser mayor o igual a cero")
            @Value("${puig.ventas.costo-reparto}")
            private double costo_reparto;
            @NotBlank(message = "Se requiere el nombre del cliente para la venta de reparto")
            @Pattern(regexp = "^[A-Z]+(?: [A-Z]+)*$",
                    message = "Nombre de cliente de venta de reparto invalido. Recuerda que debe ir en mayúsculas")
            private String nombre_cliente;
            @Valid
            @NotNull(message = "Se requiere el número de telefono del cliente")
            private Telefono telefono_cliente;

            @Override
            public Reparto instance() {
                return Reparto.builder()
                        .detalle(venta_request.detalle)
                        .forma_entrega(venta_request.forma_entrega)
                        .realizada_en(venta_request.realizada_en)
                        .fecha_venta(LocalDateTime.now())
                        .tomada_por(venta_request.tomada_por)
                        .pagos(venta_request.pagos)
                        .direccion(direccion.instance())
                        .costo_reparto(costo_reparto)
                        .nombre_cliente(nombre_cliente)
                        .telefono_cliente(telefono_cliente)
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
}