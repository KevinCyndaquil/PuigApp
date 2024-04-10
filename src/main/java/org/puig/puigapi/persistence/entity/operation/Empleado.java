package org.puig.puigapi.persistence.entity.operation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.persistence.entity.utils.Persona;
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
    private String curp;
    private Puestos puesto;
    private Tarjeta cuenta_nomina;

    @Override
    public Tipo getTipo() {
        return Tipo.EMPLEADO;
    }

    @Data
    @EqualsAndHashCode(exclude = {"fecha_alta", "fecha_baja", "estado"})
    public static class Detalle {
        @NotNull @DBRef private Empleado empleado;
        @NotNull @JsonFormat(pattern = "yyyy-MM-dd") private LocalDate fecha_alta;
        @NotNull @JsonFormat(pattern = "yyyy-MM-dd") private LocalDate fecha_baja;
        @NotNull private Estados estado = Estados.ESPERA;
    }

    public enum Estados {
        ESPERA,
        ALTA,
        BAJA,
        BAJA_TEMPORAL
    }

    @Data
    @NoArgsConstructor
    public static class Request implements PostEntity<Empleado> {
        @NotBlank(message = "Nombre de empleado no puede estar vacío")
        @Pattern(regexp = "^[A-Z]+(?: [A-Z]+)*$",
                message = "Nombre de empleado invalido. Recuerda que debe ir en mayúsculas")
        private String nombre;
        @NotBlank(message = "Apellido paterno de empleado no puedo estar vacío")
        @Pattern(regexp = "^[A-Z]+$",
                message = "Apellido paterno de empleado invalido. Recuerda que debe ir en mayúsculas")
        private String apellido_paterno;
        @Pattern(regexp = "^[A-Z]+$",
                message = "Apellido materno de empelado invalido. Recuerda que debe ir en mayúsculas")
        private String apellido_materno;
        @NotBlank(message = "RFC de empleado no puede estar vacío")
        @Pattern(regexp = "^[a-zA-Z]{4}[0-9]{6}[a-zA-Z0-9]{3}$",
                message = "RFC de empleado no es válido")
        private String rfc;
        @NotBlank(message = "Telefono de empelado no puede estar vacío")
        @Pattern(regexp = "^\\+\\([0-9]{2}\\) [0-9]{3} [0-9]{3} [0-9]{4}$",
                message = "Teléfono de empleado no es válido")
        private String telefono;
        @NotBlank(message = "Contraseña no válida. Debe llevar caracteres")
        private String password;
        @NotBlank(message = "Nickname de empleado no debe estar vacío")
        String nickname;
        @NotNull @JsonFormat(pattern = "yyyy-MM-dd") private LocalDate fecha_nacimiento;
        @JsonFormat(pattern = "yyyy-MM-dd") private LocalDate fecha_alta = LocalDate.now();
        @NotBlank(message = "Curp de empleado invalido. No debe estar vacío")
        @Pattern(regexp = "^[A-Z]{4}[0-9]{6}[HM][A-Z]{2}[A-Z]{3}[0-9A-Z][0-9]$",
                message = "Formato de curp de empleado no es válida")
        private String curp;
        @NotNull private Puestos puesto;
        @NotNull private Tarjeta.Request cuenta_nomina;

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

    public enum Puestos {
        GERENTE,
        CAJERO,
        REPARTIDOR,
        COCINERO
    }
}
