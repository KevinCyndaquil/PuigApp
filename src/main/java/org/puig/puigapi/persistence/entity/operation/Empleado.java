package org.puig.puigapi.persistence.entity.operation;

import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.persistence.entity.utils.Tarjeta;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false, exclude = {"fecha_nacimiento", "fecha_alta", "puesto", "cuenta_nomina"})
@Document(collection = "operation")
public class Empleado extends Persona {
    @Id private @NotNull String _nickname;
    private @NotNull LocalDate fecha_nacimiento;
    private @NotNull LocalDate fecha_alta = LocalDate.now();
    private @NotNull String curp;
    private @NotNull Puestos puesto;
    private @NotNull Tarjeta cuenta_nomina;

    public enum Puestos {
        GERENTE,
        EMPLEADO,
        ADMIN
    }
}
