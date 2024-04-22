package org.puig.puigapi.persistence.entity.operation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.puig.puigapi.persistence.entity.utils.*;
import org.puig.puigapi.persistence.entity.utils.data.Correo;
import org.puig.puigapi.persistence.entity.utils.data.RFC;
import org.puig.puigapi.persistence.entity.utils.data.Telefono;
import org.puig.puigapi.persistence.entity.utils.persistence.PostEntity;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    private Roles rol;
    private RazonesSociales razon;
    private RegimenesFiscales regimen;
    private CFDI cfdi;

    public enum Roles {
        CLIENTE,
        ADMINISTRADOR_WEB
    }

    @Override
    public Tipo getTipo() {
        return Tipo.USUARIO;
    }

    @Data
    @NoArgsConstructor
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
        @NotNull(message = "Se requiere el número de telefono del usuario")
        @Valid private Telefono telefono;
        @NotBlank(message = "Se requiere una contraseña para el usuario")
        @Pattern(regexp = "^.{8,}$",
                message = "Se requiere una contraseña de al menos 8 caracteres")
        private String password;
        @NotNull(message = "Se requiere un correo electrónico para el usuario")
        @Valid private Correo correo;
        @NotNull(message = "Se requiere el rol del usuario")
        private Usuario.Roles rol;
        @Valid private Set<Direccion.RequestUsuario> direcciones = new HashSet<>();
        @Valid private Set<Tarjeta.Request> tarjetas = new HashSet<>();
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
                    .direcciones(direcciones.stream()
                            .map(Direccion.RequestUsuario::instance)
                            .collect(Collectors.toSet()))
                    .tarjetas(tarjetas.stream()
                            .map(Tarjeta.Request::instance)
                            .collect(Collectors.toSet()))
                    .build();
        }
    }

    @Data
    @NoArgsConstructor
    public static class VentaRequest implements PostEntity<Usuario> {
        @NotBlank(message = "Se require un nombre para el usuario")
        @Pattern(regexp = "(?U)^[\\p{Lu}\\p{M}]+( [\\p{Lu}\\p{M}]+)*$",
                message = "Nombre de usuario invalido. Recuerda que debe ir en mayúsculas")
        private String nombre;
        @Valid private RFC rfc;
        @Valid private Correo correo;
        @NotNull(message = "Se requiere la razon social del usuario")
        private RazonesSociales razon = RazonesSociales.FISICO;

        @Override
        public Usuario instance() {
            return Usuario.builder()
                    .nombre(nombre)
                    .rfc(rfc)
                    .correo(correo)
                    .razon(razon)
                    .rol(Roles.CLIENTE)
                    .build();
        }
    }

    @Data
    @NoArgsConstructor
    public static class RepartoRequest implements PostEntity<Usuario> {
        @NotBlank(message = "Se require un nombre para el usuario")
        @Pattern(regexp = "(?U)^[\\p{Lu}\\p{M}]+( [\\p{Lu}\\p{M}]+)*$",
                message = "Nombre de usuario invalido. Recuerda que debe ir en mayúsculas")
        private String nombre;
        @Valid private RFC rfc;
        private RazonesSociales razon = RazonesSociales.FISICO;
        @NotNull(message = "Se requiere el número de telefono del cliente para realizar el reparto")
        @Valid private Telefono telefono;
        @Valid
        @NotEmpty(message = "Se requiere al menos una direccion para la venta de reparto")
        private Set<Direccion.RequestUsuario> direcciones = new HashSet<>();

        @Override
        public Usuario instance() {
            return Usuario.builder()
                    .nombre(nombre)
                    .rfc(rfc)
                    .razon(razon)
                    .telefono(telefono)
                    .direcciones(direcciones.stream()
                            .map(PostEntity::instance)
                            .collect(Collectors.toSet()))
                    .rol(Roles.CLIENTE)
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
        return id;
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
