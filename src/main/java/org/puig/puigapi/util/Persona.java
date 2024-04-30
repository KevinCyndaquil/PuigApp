package org.puig.puigapi.util;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;
import org.puig.puigapi.util.data.RFC;
import org.puig.puigapi.util.data.Telefono;
import org.puig.puigapi.util.persistence.Irrepetibe;
import org.springframework.data.annotation.Id;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Persona generica.
 */

@Data
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public abstract class Persona implements Irrepetibe<ObjectId>, UserDetails {
    private String nombre;
    private String apellido_paterno;
    private String apellido_materno;
    private RFC rfc;
    private Telefono telefono;
    @JsonProperty(access = Access.WRITE_ONLY) private String password;
    @JsonProperty(access = Access.WRITE_ONLY) private String salt;

    @JsonIgnore private String username;
    @Id private ObjectId id;

    @JsonGetter("especializado")
    public abstract Especializaciones getEspecializado();

    public enum Especializaciones {
        USUARIO,
        EMPLEADO
    }
}
