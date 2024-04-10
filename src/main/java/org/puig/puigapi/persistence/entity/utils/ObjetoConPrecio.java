package org.puig.puigapi.persistence.entity.utils;

import com.fasterxml.jackson.annotation.JsonGetter;

/**
 * Representa un objeto que contiene un precio
 */
public interface ObjetoConPrecio {
    @JsonGetter("precio")
    double getPrecio();
}
