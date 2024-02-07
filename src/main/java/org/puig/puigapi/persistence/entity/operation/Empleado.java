package org.puig.puigapi.persistence.entity.operation;

import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.persistence.entity.utils.Tarjeta;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "operation")
public class Empleado extends Persona {
    private @NotNull LocalDate fecha_nacimiento;
    private @NotNull LocalDate fecha_alta = LocalDate.now();
    private @NotNull String curp;
    private @NotNull Puestos puesto;
    private @NotNull Tarjeta cuenta_nomina;

    public Empleado(@NotNull String nombre,
                    @NotNull String apellido_paterno,
                    String apellido_materno,
                    @NotNull String rfc,
                    String telefono,
                    @NotNull LocalDate fecha_nacimiento,
                    @NotNull LocalDate fecha_alta,
                    @NotNull String curp,
                    @NotNull Puestos puesto,
                    @NotNull Tarjeta cuenta_nomina) {
        super(null, nombre, apellido_paterno, apellido_materno, rfc, telefono);
        this.fecha_nacimiento = fecha_nacimiento;
        this.fecha_alta = fecha_alta;
        this.curp = curp;
        this.puesto = puesto;
        this.cuenta_nomina = cuenta_nomina;
    }

    public enum Puestos {
        GERENTE,
        EMPLEADO,
        ADMIN
    }
}
