package org.puig.api.persistence.entity.finances;

import com.fasterxml.jackson.annotation.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import org.bson.types.ObjectId;
import org.puig.api.persistence.entity.operation.Empleado.Puestos;
import org.puig.api.util.errors.VentaInvalidaException;
import org.puig.api.persistence.entity.admin.Proveedor;
import org.puig.api.persistence.entity.operation.Usuario;
import org.puig.api.util.Articulo;
import org.puig.api.util.contable.Calculable;
import org.puig.api.persistence.entity.operation.Empleado;
import org.puig.api.persistence.entity.operation.Sucursal;
import org.puig.api.util.contable.Contable;
import org.puig.api.util.contable.Detalle;
import org.puig.api.util.data.Direccion;
import org.puig.api.util.grupos.InitInfo;
import org.puig.api.util.grupos.RepartoInfo;
import org.puig.api.util.grupos.SimpleInfo;
import org.puig.api.util.grupos.UniqueInfo;
import org.puig.api.util.persistence.Unico;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;

/**
 * Conjunto de caracteristicas que permiten el realizar un intercambio.
 * La finalidad de ofrecer un artículo o combo a sus clientes a cambio de obtener ganacia.
 */

@Data
@Document(collection = "finances")
public class Venta implements Unico {
    @Null(groups = SimpleInfo.class) @NotNull(groups = UniqueInfo.class)
    private ObjectId id;
    @Null(groups = InitInfo.class)
    private String folio;
    @NotEmpty(message = "Se deben añadir artículos a la venta_request",
            groups = InitInfo.class)
    private @Valid Detalle<Calculable<Articulo>> ticket;
    private Usuario cliente;
    @NotNull(message = "Se requiere la forma de entrega de la venta",
            groups = InitInfo.class)
    private ModosDeEntrega modo_entrega;
    private LocalDateTime fecha_venta;
    @NotNull(message = "Se require la sucursal donde se está realizando la venta",
            groups = InitInfo.class)
    private @DBRef(lazy = true) Sucursal realizada_en;
    @NotNull(message = "Se requiere el empleado que inicio sesión",
            groups = InitInfo.class)
    private @DBRef(lazy = true) Empleado tomada_por;
    @Null(message = "No se debe agregar un empleado asignado durante la venta",
            groups = InitInfo.class)
    private @DBRef(lazy = true) Empleado asignada_a;
    @NotEmpty(message = "Se deben añadir pagos a la veta",
            groups = InitInfo.class)
    private @Valid List<Pago> pagos;
    private boolean internet = false;
    @NotNull(message = "Se require el cliente reparto de la compra de reparto",
            groups = RepartoInfo.class)
    private @DBRef(lazy = true) Usuario cliente_reparto;
    @NotNull(message = "Se requiere la ubicación del reparto",
            groups = RepartoInfo.class)
    private @Valid Direccion direccion_reparto;
    @PositiveOrZero(message = "Costo de reparto debe ser mayor o igual a cero",
            groups = RepartoInfo.class)
    private double costo_reparto = 30;
    @Null(message = "No se puede agregar un repartidor durante la venta",
            groups = RepartoInfo.class)
    private @DBRef Empleado repartidor;

    public enum ModosDeEntrega {
        PARA_LLEVAR,
        EN_RESTAURANTE,
        REPARTO,
    }

    public void forEachDetalle(@NonNull Consumer<Contable<Proveedor.Producto>> consumer) {
        this.getTicket().forEach(t -> t.getDetalle()
                .getContables()
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

    @Field("monto_total")
    @JsonGetter("monto_total")
    public double getMonto_total() {
        return ticket.stream()
                .map(Calculable::getMonto)
                .reduce(0d, Double::sum) +
                (modo_entrega == ModosDeEntrega.REPARTO ? costo_reparto : 0);
    }

    @Field("pago_total")
    @JsonGetter("pago_total")
    public double getPago_total() {
        return pagos.stream()
                .map(Pago::getPago)
                .reduce(0d, Double::sum);
    }

    @JsonSetter("repartidor")
    public void setRepartidor(@NonNull Empleado repartidor) {
        if (modo_entrega == ModosDeEntrega.REPARTO)
            if (repartidor.getPuesto() != Puestos.REPARTIDOR)
                throw new VentaInvalidaException.EmpleadoInvalido(repartidor, Puestos.REPARTIDOR);
            else this.repartidor = repartidor;
        throw new VentaInvalidaException.RepartidorInvalido(repartidor, modo_entrega);
    }

    public void validar() {
        if (getMonto_total() > getPago_total())
            throw new VentaInvalidaException.MontoInvalido(this );
    }

    @Data
    public static class Pago {
        @Positive(groups = InitInfo.class)
        private double pago;
        @NotNull(message = "Se requiere ingresar el modo de pago",
                groups = InitInfo.class)
        private Modo modo = Modo.EFECTIVO;

        public enum Modo {
            EFECTIVO,
            DEBITO,
            CREDITO
        }
    }

    @Data
    public static class ReporteProducto {
        private ArticuloMenu articulo;
        private double cantidad_total;
        private double monto_total;
    }
}