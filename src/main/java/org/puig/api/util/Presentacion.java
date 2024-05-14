package org.puig.api.util;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.puig.api.util.contable.Unidades;

/**
 * Representación generica para una presentación para un producto.
 */

@Data
public class Presentacion {
    /**
     * Unidad de medida por pieza de este producto (ej. 1KG, 1 pieza, 1L, etc.)
     */
    @Positive private double peso_pieza = 1;
    @NotNull private Unidades unidad_pieza = Unidades.PIEZA;
    /**
     * Es la cantidad de piezas que contiene el empaque
     */
    @Positive private double cantidad;
    @NotNull private Empaques empaque = Empaques.BOLSA;
    /**
     * Si es verdadero, el producto es apto para ser calculado según su peso.
     * Por ejemplo, si hay un producto que por pieza pesa 20g, y hay de ese producto 200g, es factible
     * calcular su cantidad diviendo el peso total por el peso unitario.
     */
    private boolean calculable = true;

    public enum Empaques {
        PIEZA,
        PAQUETE,
        CAJA,
        CUBETA,
        BOLSA
    }
}
