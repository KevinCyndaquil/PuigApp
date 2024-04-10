package org.puig.puigapi.errors;

import lombok.Getter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.persistence.entity.finances.Venta;

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
}
