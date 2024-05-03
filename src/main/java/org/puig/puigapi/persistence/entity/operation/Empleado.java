package org.puig.puigapi.persistence.entity.operation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;
import org.puig.puigapi.util.Persona;
import org.puig.puigapi.util.data.Curp;
import org.puig.puigapi.util.data.RFC;
import org.puig.puigapi.util.data.Telefono;
import org.puig.puigapi.util.persistence.Instantiator;
import org.puig.puigapi.util.contable.Tarjeta;
import org.puig.puigapi.util.persistence.SimpleInstance;
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
@EqualsAndHashCode(callSuper = false, exclude = {"fecha_nacimiento", "puesto", "cuenta_nomina"})
@Document(collection = "operation")
public class Empleado extends Persona {
    private String nickname;
    private LocalDate fecha_nacimiento;
    private Curp curp;
    private Puestos puesto;
    private Tarjeta cuenta_nomina;

    public enum Puestos {
        GERENTE,
        CAJERO,
        REPARTIDOR,
        COCINERO,
        NONE
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
        @DBRef private Empleado empleado;
        private LocalDate fecha_alta;
        private LocalDate ultimo_cambio;
        private Empleado.Estados estado;

        public Detalle(Empleado empleado, Estados estado) {
            this.empleado = empleado;
            this.fecha_alta = LocalDate.now();
            this.estado = estado;
        }
    }

    @Data
    @NoArgsConstructor
    public static class PostRequest implements Instantiator<Empleado> {
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
        @Valid
        @NotNull(message = "Se requiere el rfc del empleado")
        private RFC rfc;
        @Valid
        private Telefono telefono;
        @NotBlank(message = "Se requiere una contraseña de al menos dos caracteres para el empleado")
        @Pattern(regexp = "^.{2,}$",
                message = "Se requiere una contraseña de al menos dos caracteres")
        private String password;
        @NotBlank(message = "Se requiere un nickname para el empleado")
        private String nickname;
        @NotNull(message = "Se requiere la fecha de nacimiento del empleado")
        private LocalDate fecha_nacimiento;
        @Valid
        @NotNull(message = "Se requiere la curp del empleado")
        private Curp curp;
        @NotNull(message = "Se requiere el puesto del empleado")
        private Puestos puesto;
        @Valid
        @NotNull(message = "Se requiere la cuenta de nomina del empleado")
        private Tarjeta.Request cuenta_nomina;
        @NotNull(message = "Se requiere la sucursal durante la alta")
        private SimpleInstance<String> sucursal;

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
                    .curp(curp)
                    .puesto(puesto)
                    .cuenta_nomina(cuenta_nomina.instance())
                    .build();
        }
    }

    @Data
    public static class UpdateEstadoRequest {
        @NotNull private SimpleInstance<ObjectId> empleado;
        @NotNull private SimpleInstance<String> sucursal;
        @NotNull private Empleado.Estados estado;
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
}
