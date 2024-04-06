package org.puig.puigapi.errors;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class CreationVentaException extends RuntimeException {
    public CreationVentaException(String message) {
        super(message);
    }

    @Contract("_, _ -> new")
    public static @NotNull CreationVentaException pagoNoAlcanzaMonto(double monto, double pago) {
        return new CreationVentaException("Pago de $%s realizado es menor al monto total de $%s"
                .formatted(monto, pago));
    }
}
