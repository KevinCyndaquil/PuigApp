package org.puig.puigapi.persistence.entity.operation;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.persistence.entity.utils.Direccion;
import org.puig.puigapi.persistence.entity.utils.Persona;
import org.puig.puigapi.persistence.entity.utils.PostEntity;
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
@EqualsAndHashCode(callSuper = false, exclude = {"direcciones", "tarjetas", "rol"})
@Document(collection = "operation")
public class Usuario extends Persona {
    @NotNull @Id private String correo;
    private Set<Direccion> direcciones;
    private Set<Tarjeta> tarjetas;
    @NotNull private Rol rol;

    @Override
    public Tipo getTipo() {
        return Tipo.USUARIO;
    }

    public record Post(@NotNull String nombre,
                       @NotNull String apellido_paterno,
                       String apellido_materno,
                       @NotNull String rfc,
                       @NotNull String telefono,
                       @NotNull String password,
                       @NotNull String correo,
                       @NotNull Rol rol) implements PostEntity<Usuario> {

        @Override
        public Usuario instance() {
            return Usuario.builder()
                    .nombre(nombre)
                    .apellido_paterno(apellido_paterno)
                    .apellido_materno(apellido_materno)
                    .rfc(rfc)
                    .telefono(telefono)
                    .password(password)
                    .correo(correo)
                    .rol(rol)
                    .build();
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(
                new SimpleGrantedAuthority(getTipo().name()),
                new SimpleGrantedAuthority(rol.name()));
    }

    @Override
    public String getUsername() {
        return correo;
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
        return correo;
    }

    public enum Rol {
        CLIENTE,
        ADMINISTRADOR_WEB
    }
}
