package org.puig.puigapi.persistence.entity.operation;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.persistence.entity.utils.Persona;
import org.puig.puigapi.persistence.entity.utils.PostEntity;
import org.puig.puigapi.persistence.entity.utils.Tarjeta;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

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
    @NotNull @Id private String nickname;
    @NotNull @JsonFormat(pattern = "yyyy-MM-dd") private LocalDate fecha_nacimiento;
    @NotNull @JsonFormat(pattern = "yyyy-MM-dd") private LocalDate fecha_alta;
    @NotNull private String curp;
    @NotNull private Puestos puesto;
    @NotNull private Tarjeta cuenta_nomina;

    @Override
    public Tipo getTipo() {
        return Tipo.EMPLEADO;
    }



    public record Post(
            @NotNull String nombre,
            @NotNull String apellido_paterno,
            String apellido_materno,
            @NotNull String rfc,
            @NotNull String telefono,
            @NotNull String password,
            @NotNull String nickname,
            @NotNull LocalDate fecha_nacimiento,
            LocalDate fecha_alta,
            @NotNull String curp,
            @NotNull Puestos puesto,
            @NotNull Tarjeta cuenta_nomina
    )
            implements PostEntity<Empleado> {

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
                    .fecha_alta(Objects.isNull(fecha_alta) ? LocalDate.now() : fecha_alta)
                    .curp(curp)
                    .puesto(puesto)
                    .cuenta_nomina(cuenta_nomina)
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
        CAJERO
    }
}
