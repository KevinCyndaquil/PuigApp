package org.puig.api.persistence.entity.operation;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import org.puig.api.util.*;
import org.puig.api.util.contable.Tarjeta;
import org.puig.api.util.data.Correo;
import org.puig.api.util.data.Direccion;
import org.puig.api.util.data.Telefono;
import org.puig.api.util.grupos.FacturaInfo;
import org.puig.api.util.grupos.InitInfo;
import org.puig.api.util.sat.CFDI;
import org.puig.api.util.sat.RazonesSociales;
import org.puig.api.util.sat.RegimenesFiscales;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Un usuario simple que funciona como cliente_reparto o cómo administrador del servicio web.
 */

@Data
@EqualsAndHashCode(callSuper = false, exclude = {"direcciones", "tarjetas", "rol"})
@Document(collection = "operation")
public class Usuario extends PuigUser {
    @NotNull(message = "Se requiere un correo electrónico para el usuario",
            groups = {InitInfo.class, FacturaInfo.class})
    private @Valid Correo correo;
    @NotNull(groups = {InitInfo.class, FacturaInfo.class})
    private @Valid Set<Direccion> direcciones;
    @Null(groups = FacturaInfo.class)
    private @Valid Set<Tarjeta> tarjetas;
    @Null(groups = FacturaInfo.class)
    @NotNull(message = "Se requiere el rol del usuario",
            groups = InitInfo.class)
    private Roles rol;
    @NotNull(message = "Se requiere la razon social del usuario",
            groups = {InitInfo.class, FacturaInfo.class})
    private RazonesSociales razon;
    @Null(groups = InitInfo.class)
    @NotNull(message = "Se requiere el regimen fiscal del cliente para la factura",
            groups = FacturaInfo.class)
    private RegimenesFiscales regimen;
    @Null(groups = InitInfo.class)
    @NotNull(message = "Se requiere el cfdi del cliente para la factura",
            groups = FacturaInfo.class)
    private CFDI cfdi;

    public enum Roles {
        CLIENTE,
        ADMINISTRADOR_WEB,
        NONE
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

    @Data
    public static class ID {
        @Valid private Correo correo;
        @Valid private Telefono telefono;
    }
}
