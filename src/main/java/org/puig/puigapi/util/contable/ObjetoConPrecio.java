package org.puig.puigapi.util.contable;

import com.fasterxml.jackson.annotation.JsonGetter;
import jakarta.validation.constraints.Positive;

/**
 * Representa un objeto que contiene un precio
 */
public interface ObjetoConPrecio {
    @Positive
    @JsonGetter("precio")
    double getPrecio();
}
