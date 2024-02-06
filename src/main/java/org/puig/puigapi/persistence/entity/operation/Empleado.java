package org.puig.puigapi.persistence.entity.operation;

import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "operation")
public class Empleado extends Persona {
    private @NotNull String apellido_paterno;
    private String apellido_materno;
    private @NotNull LocalDate fecha_nacimiento;
    private @NotNull LocalDate fecha_alta = LocalDate.now();
    private @NotNull String curp;
    private @NotNull Puestos puesto;

    public Empleado(String _id,
                    @NotNull String nombre,
                    String telefono,
                    @NotNull String rfc,
                    @NotNull String apellido_paterno,
                    String apellido_materno,
                    @NotNull LocalDate fecha_nacimiento,
                    @NotNull LocalDate fecha_alta,
                    @NotNull String curp,
                    @NotNull Puestos puesto) {
        super(_id, nombre, telefono, rfc);
        this.apellido_paterno = apellido_paterno;
        this.apellido_materno = apellido_materno;
        this.fecha_nacimiento = fecha_nacimiento;
        this.fecha_alta = fecha_alta;
        this.curp = curp;
        this.puesto = puesto;
    }

    public Empleado(String _id,
                    @NotNull String nombre,
                    String telefono,
                    @NotNull String rfc,
                    @NotNull String apellido_paterno,
                    String apellido_materno,
                    @NotNull LocalDate fecha_nacimiento,
                    @NotNull String curp,
                    @NotNull Puestos puesto) {
        super(_id, nombre, telefono, rfc);
        this.apellido_paterno = apellido_paterno;
        this.apellido_materno = apellido_materno;
        this.fecha_nacimiento = fecha_nacimiento;
        this.curp = curp;
        this.puesto = puesto;
    }

    public enum Puestos {
        GERENTE,
        EMPLEADO,
        ADMIN
    }
}
