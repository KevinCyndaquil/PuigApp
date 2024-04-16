package org.puig.puigapi.persistence.entity.operation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.puig.puigapi.persistence.entity.utils.Direccion;
import org.puig.puigapi.persistence.entity.utils.Persona;
import org.puig.puigapi.persistence.entity.utils.RazonesSociales;
import org.puig.puigapi.persistence.entity.utils.data.Correo;
import org.puig.puigapi.persistence.entity.utils.data.RFC;
import org.puig.puigapi.persistence.entity.utils.data.Telefono;
import org.puig.puigapi.persistence.entity.utils.persistence.PostEntity;
import org.puig.puigapi.persistence.entity.utils.Tarjeta;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Un usuario simple que funciona como cliente o cómo administrador del servicio web.
 */

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false, exclude = {"direcciones", "tarjetas", "rol"})
@Document(collection = "operation")
public class Usuario extends Persona {
    @JsonIgnore @Id private String id;
    private Correo correo;
    private Set<Direccion> direcciones;
    private Set<Tarjeta> tarjetas;
    private Rol rol;
    private RazonesSociales razon;

    public enum Rol {
        CLIENTE,
        ADMINISTRADOR_WEB
    }

    @Override
    public Tipo getTipo() {
        return Tipo.USUARIO;
    }

    @Data
    public static class Request implements PostEntity<Usuario> {
        @NotBlank(message = "Se require un nombre para el usuario")
        @Pattern(regexp = "(?U)^[\\p{Lu}\\p{M}]+( [\\p{Lu}\\p{M}]+)*$",
                message = "Nombre de usuario invalido. Recuerda que debe ir en mayúsculas")
        private String nombre;
        @NotBlank(message = "Se requiere un apellido paterno para el usuario")
        @Pattern(regexp = "(?U)^[\\p{Lu}\\p{M}]+$",
                message = "Apellido paterno de usuario invalido. Recuerda que debe ir en mayúsculas")
        private String apellido_paterno;
        @Pattern(regexp = "(?U)^[\\p{Lu}\\p{M}]+$",
                message = "Apellido materno de usuario invalido. Recuerda que debe ir en mayúsculas")
        private String apellido_materno;
        @Valid private RFC rfc;
        @NotNull(message = "Se requiere un número de telefono para el usuario")
        @Valid
        private Telefono telefono;
        @NotBlank(message = "Se requiere una contraseña para el usuario")
        @Pattern(regexp = "^.{8,}$", message = "Se requiere una contraseña de al menos 8 caracteres")
        private String password;
        @Valid
        @NotBlank(message = "Se requiere el correo electrónico del usuario")
        @Pattern(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$",
                message = "Correo electrónico de usuario no es válido")
        private Correo correo;
        @NotNull(message = "Se requiere el rol del usuario")
        private Rol rol;
        @NotNull(message = "Se requiere la razon social del usuario")
        private RazonesSociales razon = RazonesSociales.FISICO;

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
                    .razon(razon)
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
        return correo.getDireccion();
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
        return correo.getDireccion();
    }
}
