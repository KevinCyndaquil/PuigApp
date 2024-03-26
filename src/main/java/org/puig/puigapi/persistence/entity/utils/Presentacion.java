package org.puig.puigapi.persistence.entity.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Presentacion {
    private float peso;
    private @NotNull Envases envase = Envases.PESO;

    public enum Envases {
        PIEZA,
        BOLSA,
        CAJA,
        CUBETA,
        PESO
    }
}
