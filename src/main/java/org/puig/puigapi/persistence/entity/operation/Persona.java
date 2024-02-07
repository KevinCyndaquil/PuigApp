package org.puig.puigapi.persistence.entity.operation;

import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false, exclude = {"nombre", "apellido_paterno", "apellido_materno", "telefono"})
public abstract class Persona {
    @Id private String _id;
    private @NotNull String nombre;
    private @NotNull String apellido_paterno;
    private String apellido_materno;
    private @NotNull String rfc;
    private String telefono;
}
