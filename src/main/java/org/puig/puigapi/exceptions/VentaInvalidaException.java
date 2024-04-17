package org.puig.puigapi.exceptions;

import lombok.Getter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.persistence.entity.finances.Venta;
import org.puig.puigapi.persistence.entity.operation.Empleado;

@Getter
public class VentaInvalidaException extends RuntimeException {
    protected final String hint;

    public VentaInvalidaException(String message, String hint) {
        super(message);
        this.hint = hint;
    }

    @Contract("_ -> new")
    public static @NotNull VentaInvalidaException pagoInferiorAMonto(@NotNull Venta venta) {
        return new VentaInvalidaException("Pago de $%s realizado es menor al monto total de $%s"
                .formatted(venta.getPago_total(), venta.getMonto_total()),
                "El pago es inferior al monto, pide al cliente completar su venta_request");
    }

    @Contract("_ -> new")
    public static @NotNull VentaInvalidaException empleadoNoEsRepartidor(@NotNull Empleado empleado) {
        return new VentaInvalidaException("Empleado %s:%s no está contratado como repartidor, sino como %s"
                .formatted(empleado.getNickname(), empleado.getNombre(), empleado.getPuesto()),
                "Asigna a un empleado que sea un repartidor para esta venta");
    }
}
