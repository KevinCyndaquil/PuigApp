package org.puig.puigapi.persistence.entity.utils;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.persistence.entity.utils.persistence.PostEntity;

/**
 * Representación generica para una presentación para un producto.
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Presentacion {
    private float peso;
    private Empaques envase = Empaques.PESO;

    public enum Empaques {
        PIEZA,
        BOLSA,
        CAJA,
        CUBETA,
        PESO
    }

    @Data
    @NoArgsConstructor
    public static class Request implements PostEntity<Presentacion> {
        @Positive(message = "El peso de una presentación debe ser mayor a cero")
        private float peso;
        @NotNull private Empaques envase = Empaques.PESO;

        @Override
        public Presentacion instance() {
            return Presentacion.builder()
                    .peso(peso)
                    .envase(envase)
                    .build();
        }
    }
}
