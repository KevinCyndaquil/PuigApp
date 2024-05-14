package org.puig.api.util.errors;

import lombok.Getter;
import lombok.NonNull;
import org.puig.api.persistence.entity.operation.Empleado;
import org.puig.api.persistence.entity.operation.Sucursal;
import org.springframework.data.mongodb.MongoTransactionException;

@Getter
public class AltaEmpleadoInterrumpidaException extends MongoTransactionException {
    private final Reasons reason;

    public AltaEmpleadoInterrumpidaException(Reasons reason, @NonNull Sucursal sucursal, @NonNull Empleado empleado) {
        super("Error al dar de alta al empleado %s a la sucursal %s"
                .formatted(empleado.getNombre(), sucursal.getNombre()));
        this.reason = reason;
    }

    public enum Reasons {
        TRANSACTION_ERROR,
        ID_ERROR
    }
}
