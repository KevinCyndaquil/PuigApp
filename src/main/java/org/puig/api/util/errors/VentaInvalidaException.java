package org.puig.api.util.errors;

import lombok.Getter;
import lombok.NonNull;
import org.puig.api.persistence.entity.finances.Venta;
import org.puig.api.persistence.entity.operation.Empleado;
import org.puig.api.persistence.entity.operation.Empleado.Puestos;

@Getter
public class VentaInvalidaException extends RuntimeException {
    protected final String hint;

    public VentaInvalidaException(String message, String hint) {
        super(message);
        this.hint = hint;
    }

    public static class MontoInvalido extends VentaInvalidaException {
        public MontoInvalido(@NonNull Venta venta) {
            super("Pago de $%s realizado es menor al monto total de $%s"
                            .formatted(venta.getPago_total(), venta.getMonto_total()),
                    "El pago es inferior al monto, pide al cliente_reparto completar su venta_request");
        }
    }

    public static class EmpleadoInvalido extends VentaInvalidaException {
        public EmpleadoInvalido(@NonNull Empleado empleado, Puestos puestoEsperado) {
            super("Empleado %s:%s no está contratado como %s, sino como %s"
                            .formatted(
                                    empleado.getNickname(),
                                    empleado.getNombre(),
                                    puestoEsperado,
                                    empleado.getPuesto()),
                    "Asigna a un empleado que sea un %s para esta venta"
                            .formatted(puestoEsperado));
        }
    }

    public static class RepartidorInvalido extends VentaInvalidaException {
        public RepartidorInvalido(@NonNull Empleado repartidor, @NonNull Venta.ModosDeEntrega modoVenta) {
            super("Repartidor %s:%s no puede asignarse a una venta realizada como %s"
                    .formatted(repartidor.getNickname(), repartidor.getNombre(), modoVenta),
                    "Talvez estás interesado en una venta distinta");
        }
    }
}
