package org.puig.puigapi.exceptions;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.persistence.entity.operation.Empleado;
import org.puig.puigapi.persistence.entity.operation.Sucursal;
import org.springframework.data.mongodb.MongoTransactionException;

@Getter
public class AltaEmpleadoInterrumpidaException extends MongoTransactionException {
    private final Reasons reason;

    public AltaEmpleadoInterrumpidaException(Reasons reason, @NotNull Sucursal sucursal, @NotNull Empleado empleado) {
        super("Error al dar de alta al empleado %s a la sucursal %s"
                .formatted(empleado.getNombre(), sucursal.getNombre()));
        this.reason = reason;
    }

    public enum Reasons {
        TRANSACTION_ERROR,
        ID_ERROR
    }
}
