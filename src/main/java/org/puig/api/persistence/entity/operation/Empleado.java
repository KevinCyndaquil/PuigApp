package org.puig.api.persistence.entity.operation;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.puig.api.util.PuigUser;
import org.puig.api.util.data.Curp;
import org.puig.api.util.grupos.InitInfo;
import org.puig.api.util.contable.Tarjeta;
import org.puig.api.util.persistence.SimpleInstance;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;

/**
 * Persona que trabaja en Pollos Puig, la cual realiza una función importante dentro del
 * restaurante.
 */

@Data
@EqualsAndHashCode(callSuper = false, exclude = {"fecha_nacimiento", "puesto", "cuenta_nomina"})
@Document(collection = "operation")
public class Empleado extends PuigUser {
    @NotBlank(message = "Se requiere un nickname para el empleado",
            groups = InitInfo.class)
    private String nickname;
    @NotNull(message = "Se requiere la fecha de nacimiento del empleado",
            groups = InitInfo.class)
    private LocalDate fecha_nacimiento;
    @NotNull(message = "Se requiere la curp del empleado",
            groups = InitInfo.class)
    private @Valid Curp curp;
    @NotNull(message = "Se requiere el puesto del empleado",
            groups = InitInfo.class)
    private Puestos puesto;
    @NotNull(message = "Se requiere la cuenta de nomina del empleado",
            groups = InitInfo.class)
    private @Valid Tarjeta cuenta_nomina;

    public enum Puestos {
        GERENTE,
        CAJERO,
        REPARTIDOR,
        COCINERO
    }

    public enum Estados {
        ALTA,
        VACACIONES,
        SUSPENDIDO,
        TRANSFERIDO,
        BAJA
    }

    @Data
    @NoArgsConstructor
    @EqualsAndHashCode(exclude = {"fecha_alta", "ultimo_cambio", "estado"})
    public static class Detalle {
        @NotNull(message = "Se requiere un empleado",
                groups = InitInfo.class)
        private @DBRef Empleado empleado;
        @NotNull(groups = InitInfo.class)
        private LocalDate fecha_alta;
        private LocalDate ultimo_cambio;
        @NotNull(groups = InitInfo.class)
        private Empleado.Estados estado;

        public Detalle(Empleado empleado, Estados estado) {
            this.empleado = empleado;
            this.fecha_alta = LocalDate.now();
            this.estado = estado;
        }
    }

    @Override
    public Especializaciones getEspecializado() {
        return Especializaciones.EMPLEADO;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Set.of(
                new SimpleGrantedAuthority(getEspecializado().name()),
                new SimpleGrantedAuthority(puesto.name()));
    }

    @Override
    public String getUsername() {
        return nickname;
    }

    @Data
    public static class UpdateEstadoRequest {
        @NotNull private SimpleInstance empleado;
        @NotNull private SimpleInstance sucursal;
        @NotNull private Empleado.Estados estado;
    }

    public record Asignar(
            @NotNull(message = "Se requiere el id de la venta")
            SimpleInstance venta,
            @NotNull(message = "Se requiere el id del empleado")
            SimpleInstance empleado) {}
}
