package org.puig.puigapi.persistence.entity.operation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import lombok.*;
import org.jetbrains.annotations.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Persona {
    private @NotNull String nombre;
    private @NotNull String apellido_paterno;
    private String apellido_materno;
    private @NotNull String rfc;
    private String telefono;
    @JsonProperty(access = Access.WRITE_ONLY)
    private @NotNull String password;
    @JsonProperty(access = Access.WRITE_ONLY)
    private String salt;
}
