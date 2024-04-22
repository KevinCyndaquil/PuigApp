package org.puig.puigapi.persistence.entity.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.annotation.JsonSetter;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.persistence.entity.utils.persistence.Irrepetibe;
import org.springframework.data.mongodb.core.mapping.DBRef;

/**
 * Implementación de detallable, contiene un objeto, una cantidad y un monto.
 * @param <Obj> objeto a ser detallado.
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"cantidad", "monto"})
public class DetalleDe<Obj extends ObjetoConPrecio & Irrepetibe<?>>
        implements Detallable<Obj> {
    @NotNull @DBRef protected Obj objeto;
    @Positive(message = "Cantidad dentro del detalle debe ser mayor a cero")
    protected double cantidad;
    @JsonProperty(access = Access.READ_ONLY) private double monto;

    @Override
    public double getMonto() {
        return Detallable.super.getMonto();
    }

    @JsonSetter("objeto")
    public void setObjeto(Obj objeto) {
        this.objeto = objeto;
        this.monto = getMonto();
    }
}