package org.puig.api.util;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.bson.types.ObjectId;
import org.puig.api.util.data.RFC;
import org.puig.api.util.data.Telefono;
import org.puig.api.util.grupos.FacturaInfo;
import org.puig.api.util.grupos.InitInfo;
import org.puig.api.util.grupos.SimpleInfo;
import org.puig.api.util.grupos.UniqueInfo;
import org.puig.api.util.persistence.Nombrable;
import org.puig.api.util.persistence.Unico;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Persona generica.
 */

@Data
public abstract class PuigUser implements Unico, Nombrable, UserDetails {
    @Null(groups = SimpleInfo.class) @NotNull(groups = UniqueInfo.class)
    private @Id ObjectId id;
    @NotBlank(message = "Se requiere un nombre",
            groups = {InitInfo.class, FacturaInfo.class})
    @Pattern(regexp = "(?U)^[\\p{Lu}\\p{M}]+( [\\p{Lu}\\p{M}]+)*$",
            message = "Nombre invalido. Recuerda que debe ir en mayúsculas",
            groups = {InitInfo.class, FacturaInfo.class})
    private String nombre;
    @NotBlank(message = "Se requiere el apellido paterno",
            groups = {InitInfo.class, FacturaInfo.class})
    @Pattern(regexp = "(?U)^[\\p{Lu}\\p{M}]+$",
            message = "Apellido paterno invalido. Recuerda que debe ir en mayúsculas",
            groups = {InitInfo.class, FacturaInfo.class})
    private String apellido_paterno;
    @Pattern(regexp = "(?U)^[\\p{Lu}\\p{M}]+$",
            message = "Apellido materno invalido. Recuerda que debe ir en mayúsculas",
            groups = {InitInfo.class, FacturaInfo.class})
    private String apellido_materno;
    @NotNull(message = "Se requiere el rfc del usuario",
            groups = {InitInfo.class, FacturaInfo.class})
    private @Valid RFC rfc;
    @NotBlank(message = "Se requiere una contraseña de al menos dos caracteres para el empleado",
            groups = InitInfo.class)
    @Pattern(regexp = "^.{2,}$",
            message = "Se requiere una contraseña de al menos dos caracteres",
            groups = {InitInfo.class, FacturaInfo.class})
    private @Valid Telefono telefono;
    @Null(groups = FacturaInfo.class)
    @NotBlank(message = "Se requiere una contraseña de al menos dos caracteres para el empleado",
            groups = InitInfo.class)
    @Pattern(regexp = "^.{2,}$",
            message = "Se requiere una contraseña de al menos dos caracteres",
            groups = InitInfo.class)
    @JsonProperty(access = Access.WRITE_ONLY) private String password;

    @JsonIgnore private String salt;
    @JsonIgnore private String username;

    @Field("especializado")
    @JsonGetter("especializado")
    public abstract Especializaciones getEspecializado();

    public enum Especializaciones {
        USUARIO,
        EMPLEADO
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
