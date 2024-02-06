package org.puig.puigapi.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
@AllArgsConstructor
public class DatoBanco {
    private @NotNull String nombre;
    private String numero;
}
