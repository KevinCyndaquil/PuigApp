package org.puig.puigapi.persistence.entity.utils;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.puig.puigapi.persistence.entity.utils.persistence.PostEntity;

/**
 * Representación generica para una presentación para un producto.
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Presentacion {
    /**
     * Peso del producto en gramos
     */
    private double peso_pieza;
    /**
     * Es la cantidad de piezas que contiene el empaque
     */
    private int cantidad;
    private Empaques empaque = Empaques.PESO;
    /**
     * Si es verdadero, el producto es apto para ser calculado según su peso.
     * Por ejemplo, si hay un producto que por pieza pesa 20g, y hay de ese producto 200g, es factible
     * calcular su cantidad diviendo el peso total por el peso unitario.
     */
    private boolean calculable;

    public enum Empaques {
        PIEZA,
        PAQUETE,
        CAJA,
        CUBETA,
        PESO
    }

    @Data
    @NoArgsConstructor
    public static class Request implements PostEntity<Presentacion> {
        @NotNull(message = "Se requiere el peso de una pieza")
        @Positive(message = "El peso de cada pieza (gramos) de una presentación debe ser mayor a cero")
        private Double peso_pieza;
        @NotNull(message = "Se requiere la cantidad de piezas")
        @Positive(message = "Cantidad de piezas en presetación debe ser mayor a cero")
        private Integer cantidad = 1;
        @NotNull(message = "Se requiere el tipo de empaque de la presentació<n")
        private Empaques empaque = Empaques.PESO;
        private boolean calculable = true;

        @Override
        public Presentacion instance() {
            return Presentacion.builder()
                    .peso_pieza(peso_pieza)
                    .empaque(empaque)
                    .cantidad(cantidad)
                    .calculable(calculable)
                    .build();
        }
    }
}
