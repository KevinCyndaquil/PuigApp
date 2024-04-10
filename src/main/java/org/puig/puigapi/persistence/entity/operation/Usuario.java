package org.puig.puigapi.persistence.entity.operation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.persistence.entity.utils.Direccion;
import org.puig.puigapi.persistence.entity.utils.Persona;
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
    @Id private String correo;
    private Set<Direccion> direcciones;
    private Set<Tarjeta> tarjetas;
    private Rol rol;

    @Override
    public Tipo getTipo() {
        return Tipo.USUARIO;
    }

    @Data
    public static class Request implements PostEntity<Usuario> {
        @NotBlank(message = "Nombre de usuario no puede estar vacío")
        @Pattern(regexp = "^[A-Z]+(?: [A-Z]+)*$",
                message = "Nombre de usuario invalido. Recuerda que debe ir en mayúsculas")
        private String nombre;
        @NotBlank(message = "Apellido paterno de usuario no puedo estar vacío")
        @Pattern(regexp = "^[A-Z]+$",
                message = "Apellido paterno de usuario invalido. Recuerda que debe ir en mayúsculas")
        private String apellido_paterno;
        @Pattern(regexp = "^[A-Z]+$",
                message = "Apellido materno de usuario invalido. Recuerda que debe ir en mayúsculas")
        private String apellido_materno;
        @NotBlank(message = "RFC de usuario no puede estar vacío")
        @Pattern(regexp = "^[a-zA-Z]{4}[0-9]{6}[a-zA-Z0-9]{3}$",
                message = "RFC de usuario no es válido")
        private String rfc;
        @NotBlank(message = "Telefono de empelado no puede estar vacío")
        @Pattern(regexp = "^\\+\\([0-9]{2}\\) [0-9]{3} [0-9]{3} [0-9]{4}$",
                message = "Teléfono de empleado no es válido")
        private String telefono;
        @NotBlank(message = "Contraseña de usuario no valida, debe al menos contener un carácter")
        private String password;
        @Pattern(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$",
                message = "Correo electrónico de usuario no es válido")
        private String correo;
        @NotNull private Rol rol;

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
