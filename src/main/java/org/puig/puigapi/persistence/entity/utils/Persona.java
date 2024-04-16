package org.puig.puigapi.persistence.entity.utils;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.puig.puigapi.persistence.entity.utils.data.RFC;
import org.puig.puigapi.persistence.entity.utils.data.Telefono;
import org.puig.puigapi.persistence.entity.utils.persistence.Irrepetibe;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Persona generica.
 */

@Data
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public abstract class Persona implements Irrepetibe<String>, UserDetails {
    @JsonIgnore private String username;

    private String nombre;
    private String apellido_paterno;
    private String apellido_materno;
    private RFC rfc;
    private Telefono telefono;
    @JsonProperty(access = Access.WRITE_ONLY) private String password;
    @JsonProperty(access = Access.WRITE_ONLY) private String salt;

    @JsonGetter("especializado")
    public abstract Tipo getTipo();

    public enum Tipo {
        USUARIO,
        EMPLEADO
    }
}
