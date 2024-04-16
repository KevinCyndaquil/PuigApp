package org.puig.puigapi.exceptions;

import lombok.Getter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.persistence.entity.finances.Venta;
import org.puig.puigapi.persistence.entity.operation.Empleado;

@Getter
public class CreacionVentaException extends RuntimeException {
    protected final String hint;

    public CreacionVentaException(String message, String hint) {
        super(message);
        this.hint = hint;
    }

    @Contract("_ -> new")
    public static @NotNull CreacionVentaException pagoInferiorAMonto(@NotNull Venta venta) {
        return new CreacionVentaException("Pago de $%s realizado es menor al monto total de $%s"
                .formatted(venta.getPago_total(), venta.getMonto_total()),
                "El pago es inferior al monto, pide al cliente completar su venta_request");
    }

    @Contract("_ -> new")
    public static @NotNull CreacionVentaException empleadoNoEsRepartidor(@NotNull Empleado empleado) {
        return new CreacionVentaException("Empleado %s:%s no está contratado como repartidor, sino como %s"
                .formatted(empleado.getNickname(), empleado.getNombre(), empleado.getPuesto()),
                "Asigna a un empleado que sea un repartidor para esta venta");
    }
}
