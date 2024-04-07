package org.puig.puigapi.persistence.entity.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public abstract class Persona implements Irrepetibe<String>, UserDetails {
    @JsonIgnore private String username;

    @NotNull private String nombre;
    @NotNull private String apellido_paterno;
    private String apellido_materno;
    @NotNull private String rfc;
    private String telefono;
    @NotNull @JsonProperty(access = Access.WRITE_ONLY) private String password;
    @JsonProperty(access = Access.WRITE_ONLY) private String salt;

    public abstract Tipo getTipo();

    public enum Tipo {
        USUARIO,
        EMPLEADO
    }
}
