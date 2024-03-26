package org.puig.puigapi.persistence.entity.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.persistence.entity.operation.Empleado;
import org.puig.puigapi.persistence.entity.operation.Usuario;
import org.springframework.security.core.userdetails.UserDetails;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "tipo"
)

@JsonSubTypes({
        @JsonSubTypes.Type(value = Usuario.class, name = "USUARIO"),
        @JsonSubTypes.Type(value = Empleado.class, name = "EMPLEADO")
})

@Data
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Persona implements UserDetails {
    private @JsonIgnore String username;

    private @NotNull String nombre;
    private @NotNull String apellido_paterno;
    private String apellido_materno;
    private @NotNull String rfc;
    private String telefono;
    private @JsonProperty(access = Access.WRITE_ONLY) @NotNull String password;
    private @JsonProperty(access = Access.WRITE_ONLY) String salt;
    private @JsonProperty(access = Access.WRITE_ONLY) @NotNull Tipo tipo;

    public abstract Tipo getTipo();

    public enum Tipo {
        USUARIO,
        EMPLEADO
    }
}
