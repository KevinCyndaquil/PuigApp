package org.puig.puigapi.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"telefono"})
public abstract class Social {
    private String telefono;
    private @NotNull String rfc;
    private DatoBanco datos_bancarios;
}
