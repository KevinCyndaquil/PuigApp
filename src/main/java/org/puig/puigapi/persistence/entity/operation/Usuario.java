package org.puig.puigapi.persistence.entity.operation;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.puig.puigapi.util.*;
import org.puig.puigapi.util.contable.Tarjeta;
import org.puig.puigapi.util.data.Correo;
import org.puig.puigapi.util.data.Direccion;
import org.puig.puigapi.util.data.RFC;
import org.puig.puigapi.util.data.Telefono;
import org.puig.puigapi.util.persistence.Instantiator;
import org.puig.puigapi.util.sat.CFDI;
import org.puig.puigapi.util.sat.RazonesSociales;
import org.puig.puigapi.util.sat.RegimenesFiscales;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Un usuario simple que funciona como cliente_reparto o cómo administrador del servicio web.
 */

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false, exclude = {"direcciones", "tarjetas", "rol"})
@Document(collection = "operation")
public class Usuario extends Persona {
    private Correo correo;
    private Set<Direccion> direcciones;
    private Set<Tarjeta> tarjetas;
    private Roles rol;
    private RazonesSociales razon;
    private RegimenesFiscales regimen;
    private CFDI cfdi;

    public enum Roles {
        CLIENTE,
        ADMINISTRADOR_WEB,
        NONE
    }

    @Data
    @NoArgsConstructor
    public static class PostRequest implements Instantiator<Usuario> {
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
        @Valid
        @NotNull(message = "Se requiere el número de telefono del usuario")
        private Telefono telefono;
        @NotBlank(message = "Se requiere una contraseña para el usuario")
        @Pattern(regexp = "^.{8,}$",
                message = "Se requiere una contraseña de al menos 8 caracteres")
        private String password;
        @Valid
        @NotNull(message = "Se requiere un correo electrónico para el usuario")
        private Correo correo;
        @NotNull(message = "Se requiere el rol del usuario")
        private Usuario.Roles rol;
        @NotNull(message = "Se requiere la razon social del usuario")
        private RazonesSociales razon = RazonesSociales.FISICO;
        @Valid private Set<Direccion.PostRequest> direcciones = new HashSet<>();
        @Valid private Set<Tarjeta.Request> tarjetas = new HashSet<>();

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
                            .map(Direccion.PostRequest::instance)
                            .collect(Collectors.toSet()))
                    .tarjetas(tarjetas.stream()
                            .map(Tarjeta.Request::instance)
                            .collect(Collectors.toSet()))
                    .build();
        }
    }

    @Data
    @NoArgsConstructor
    public static class FacturableRequest implements Instantiator<Usuario> {
        @NotBlank(message = "Se require un nombre para el cliente")
        @Pattern(regexp = "(?U)^[\\p{Lu}\\p{M}]+( [\\p{Lu}\\p{M}]+)*$",
                message = "Nombre de cliente invalido. Recuerda que debe ir en mayúsculas")
        private String nombre;
        @Pattern(regexp = "(?U)^[\\p{Lu}\\p{M}]+$",
                message = "Apellido paterno de usuario invalido. Recuerda que debe ir en mayúsculas")
        private String apellido_paterno;
        @Pattern(regexp = "(?U)^[\\p{Lu}\\p{M}]+$",
                message = "Apellido materno de usuario invalido. Recuerda que debe ir en mayúsculas")
        private String apellido_materno;
        @Valid
        @NotNull(message = "Se requiere el rfc del usuario para la factura")
        private RFC rfc;
        @Valid private Telefono telefono;
        @Valid private Correo correo;
        @NotNull(message = "Se requiere la razon social del cliente")
        private RazonesSociales razon = RazonesSociales.FISICO;
        @NotNull(message = "Se requiere el regimen fiscal del cliente para la factura")
        private RegimenesFiscales regimen;
        @NotNull(message = "Se requiere el cfdi del cliente para la factura")
        private CFDI cfdi;
        @NotNull(message = "Se requiere una dirección para la factura")
        private Direccion.ParaFactura direccion;

        @Override
        public Usuario instance() {
            return Usuario.builder()
                    .nombre(nombre)
                    .rfc(rfc)
                    .correo(correo)
                    .telefono(telefono)
                    .razon(razon)
                    .rol(Roles.CLIENTE)
                    .regimen(regimen)
                    .cfdi(cfdi)
                    .direcciones(Set.of(direccion.instance()))
                    .build();
        }
    }

    @Data
    @NoArgsConstructor
    public static class ClienteRepartoRequest implements Instantiator<Usuario> {
        @NotBlank(message = "Se require un nombre para el cliente")
        @Pattern(regexp = "(?U)^[\\p{Lu}\\p{M}]+( [\\p{Lu}\\p{M}]+)*$",
                message = "Nombre de cliente invalido. Recuerda que debe ir en mayúsculas")
        private String nombre;
        @NotBlank(message = "Se require un apellido paterno para el cliente")
        @Pattern(regexp = "(?U)^[\\p{Lu}\\p{M}]+$",
                message = "Apellido paterno de usuario invalido. Recuerda que debe ir en mayúsculas")
        private String apellido_paterno;
        @Pattern(regexp = "(?U)^[\\p{Lu}\\p{M}]+$",
                message = "Apellido materno de usuario invalido. Recuerda que debe ir en mayúsculas")
        private String apellido_materno;
        @Valid private RFC rfc;
        @Valid private Correo correo;
        @Valid
        @NotNull(message = "Se requiere el número de telefono del cliente para el reparto")
        private Telefono telefono;
        @NotEmpty(message = "Se requiere al menos una direccion para la venta de reparto")
        private Set<Direccion.PostRequest> direcciones = new HashSet<>();

        @Override
        public Usuario instance() {
            return Usuario.builder()
                    .nombre(nombre)
                    .apellido_paterno(apellido_paterno)
                    .apellido_materno(apellido_materno)
                    .rfc(rfc)
                    .correo(correo)
                    .telefono(telefono)
                    .direcciones(direcciones.stream()
                            .map(Instantiator::instance)
                            .collect(Collectors.toSet()))
                    .rol(Roles.CLIENTE)
                    .build();
        }
    }

    @Override
    public Especializaciones getEspecializado() {
        return Especializaciones.USUARIO;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(
                new SimpleGrantedAuthority(getEspecializado().name()),
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

    @Data
    public static class ID {
        @Valid private Correo correo;
        @Valid private Telefono telefono;
    }
}
