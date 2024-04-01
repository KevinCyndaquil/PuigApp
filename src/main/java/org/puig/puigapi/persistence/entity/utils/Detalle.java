package org.puig.puigapi.persistence.entity.utils;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
@EqualsAndHashCode(exclude = {"cantidad", "monto"})
public class Detalle <A extends ObjetoConPrecio> {
    private final A object;
    private final int cantidad;
    private final double monto;

    @Builder
    @JsonCreator
    public Detalle(@NotNull A object,
                   int cantidad) {
        this.object = object;
        this.cantidad = cantidad;
        this.monto = object.getPrecio() * cantidad;
    }
}