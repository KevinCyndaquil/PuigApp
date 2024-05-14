package org.puig.api.util.contable;

import com.fasterxml.jackson.annotation.JsonGetter;
import jakarta.validation.constraints.Positive;
import org.puig.api.util.grupos.InitInfo;

/**
 * Representa un objeto que contiene un precio
 */
public interface ObjetoConPrecio {
    @Positive(groups = InitInfo.class)
    @JsonGetter("precio")
    double getPrecio();
}
