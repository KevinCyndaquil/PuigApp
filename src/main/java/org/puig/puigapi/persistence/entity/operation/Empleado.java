package org.puig.puigapi.persistence.entity.operation;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.persistence.entity.utils.Persona;
import org.puig.puigapi.persistence.entity.utils.Tarjeta;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

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
    @Id private @NotNull String _nickname;
    private @NotNull LocalDate fecha_nacimiento;
    private @NotNull LocalDate fecha_alta = LocalDate.now();
    private @NotNull String curp;
    private @NotNull Puestos puesto;
    private @NotNull Tarjeta cuenta_nomina;

    @Override
    public Tipo getTipo() {
        return Tipo.EMPLEADO;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(
                new SimpleGrantedAuthority(getTipo().name()),
                new SimpleGrantedAuthority(puesto.name()));
    }

    @Override
    public String getUsername() {
        return _nickname;
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

    public enum Puestos {
        GERENTE,
        CAJERO
    }
}
