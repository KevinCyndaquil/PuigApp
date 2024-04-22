package org.puig.puigapi.persistence.entity.operation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.puig.puigapi.persistence.entity.utils.Persona;
import org.puig.puigapi.persistence.entity.utils.data.Curp;
import org.puig.puigapi.persistence.entity.utils.data.RFC;
import org.puig.puigapi.persistence.entity.utils.data.Telefono;
import org.puig.puigapi.persistence.entity.utils.persistence.PostEntity;
import org.puig.puigapi.persistence.entity.utils.Tarjeta;
import org.springframework.data.annotation.Id;
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

@JsonIgnoreProperties(value = { "target" })

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false, exclude = {
        "fecha_nacimiento",
        "fecha_alta",
        "puesto",
        "cuenta_nomina"})
@Document(collection = "operation")
public class Empleado extends Persona {
    @Id private String nickname;
    @JsonFormat(pattern = "yyyy-MM-dd") private LocalDate fecha_nacimiento;
    @JsonFormat(pattern = "yyyy-MM-dd") private LocalDate fecha_alta;
    private Curp curp;
    private Puestos puesto;
    private Tarjeta cuenta_nomina;

    public enum Puestos {
        GERENTE,
        CAJERO,
        REPARTIDOR,
        COCINERO
    }

    public Detalle generarDetalle(EstadosEmpresa estado) {
        return new Detalle(this, LocalDate.now(), null, estado);
    }

    @Override
    public Tipo getTipo() {
        return Tipo.EMPLEADO;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(exclude = {"fecha_alta", "fecha_baja", "estado"})
    public static class Detalle {
        @DBRef private Empleado empleado;
        @JsonFormat(pattern = "yyyy-MM-dd") private LocalDate fecha_alta;
        @JsonFormat(pattern = "yyyy-MM-dd") private LocalDate fecha_baja;
        private EstadosEmpresa estado;

        @Data
        @NoArgsConstructor
        public static class Request implements PostEntity<Detalle> {
            @NotNull(message = "Se requiere el empleado para generar un detalle")
            private Empleado empleado;
            @NotNull(message = "Se requiere la fecha de alta para generar un detalle")
            private LocalDate fecha_alta = LocalDate.now();
            @NotNull(message = "Se requiere un estado del empleado para generar un detalle")
            private Empleado.EstadosEmpresa estado;

            @Override
            public Detalle instance() {
                return Detalle.builder()
                        .empleado(empleado)
                        .fecha_alta(fecha_alta)
                        .estado(estado)
                        .build();
            }
        }
    }

    public enum EstadosEmpresa {
        VACACIONES,
        ALTA,
        BAJA,
        BAJA_TEMPORAL
    }

    @Data
    @NoArgsConstructor
    public static class Request implements PostEntity<Empleado> {
        @NotBlank(message = "Se requiere un nombre para el empleado")
        @Pattern(regexp = "(?U)^[\\p{Lu}\\p{M}]+( [\\p{Lu}\\p{M}]+)*$",
                message = "Nombre de empleado invalido. Recuerda que debe ir en mayúsculas")
        private String nombre;
        @NotBlank(message = "Se requiere el apellido paterno del empleado")
        @Pattern(regexp = "(?U)^[\\p{Lu}\\p{M}]+$",
                message = "Apellido paterno de empleado invalido. Recuerda que debe ir en mayúsculas")
        private String apellido_paterno;
        @Pattern(regexp = "(?U)^[\\p{Lu}\\p{M}]+$",
                message = "Apellido materno de empelado invalido. Recuerda que debe ir en mayúsculas")
        private String apellido_materno;
        @NotNull(message = "Se requiere el rfc del empleado")
        @Valid private RFC rfc;
        @Valid private Telefono telefono;
        @NotBlank(message = "Se requiere una contraseña de al menos dos caracteres para el empleado")
        @Pattern(regexp = "^.{2,}$", message = "Se requiere una contraseña de al menos dos caracteres")
        private String password;
        @NotBlank(message = "Se requiere un nickname para el empleado")
        private String nickname;
        @NotNull(message = "Se requiere la fecha de nacimiento del empleado")
        private LocalDate fecha_nacimiento;
        private LocalDate fecha_alta = LocalDate.now();
        @NotNull(message = "Se requiere la curp del empleado")
        @Valid private Curp curp;
        @NotNull(message = "Se requiere el puesto del empleado")
        private Puestos puesto;
        @NotNull(message = "Se requiere la cuenta de nomina del empleado")
        @Valid private Tarjeta.Request cuenta_nomina;
        @NotNull private Sucursal sucursal_alta;

        @Override
        public Empleado instance() {
            return Empleado.builder()
                    .nombre(nombre)
                    .apellido_paterno(apellido_paterno)
                    .apellido_materno(apellido_materno)
                    .rfc(rfc)
                    .telefono(telefono)
                    .password(password)
                    .nickname(nickname)
                    .fecha_nacimiento(fecha_nacimiento)
                    .fecha_alta(fecha_alta)
                    .curp(curp)
                    .puesto(puesto)
                    .cuenta_nomina(cuenta_nomina.instance())
                    .build();
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Set.of(
                new SimpleGrantedAuthority(getTipo().name()),
                new SimpleGrantedAuthority(puesto.name()));
    }

    @Override
    public String getUsername() {
        return nickname;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getId() {
        return nickname;
    }

    @Data
    @NoArgsConstructor
    public static class Updater {
        @NotNull private Empleado empleado;
        @NotNull private Sucursal sucursal;
        @NotNull private Empleado.EstadosEmpresa estado;
    }
}
