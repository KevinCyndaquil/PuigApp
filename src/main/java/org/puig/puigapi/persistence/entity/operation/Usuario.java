package org.puig.puigapi.persistence.entity.operation;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.puig.puigapi.persistence.entity.utils.Direccion;
import org.puig.puigapi.persistence.entity.utils.Persona;
import org.puig.puigapi.persistence.entity.utils.Tarjeta;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false, exclude = {"direcciones", "tarjetas", "rol"})
@Document(collection = "operation")
public class Usuario extends Persona {
    @Id private String _correo;
    private Set<Direccion> direcciones;
    private Set<Tarjeta> tarjetas;
    private Rol rol = Rol.CLIENTE;

    @Override
    public Tipo getTipo() {
        return Tipo.USUARIO;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(
                new SimpleGrantedAuthority(getTipo().name()),
                new SimpleGrantedAuthority(rol.name()));
    }

    @Override
    public String getUsername() {
        return _correo;
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

    public enum Rol {
        CLIENTE,
        ADMINISTRADOR_WEB
    }
}
