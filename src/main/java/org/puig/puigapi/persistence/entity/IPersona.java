package org.puig.puigapi.persistence.entity;

import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.Id;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@EqualsAndHashCode(exclude = {"telefono"})
public class IPersona {
    @Id private String _id;
    private @NotNull String nombre;
    private String telefono;
    private @NotNull String rfc;
}
