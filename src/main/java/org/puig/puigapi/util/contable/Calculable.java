package org.puig.puigapi.util.contable;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.puig.puigapi.util.persistence.Irrepetibe;

/**
 * Representa cualquier objeto completo que requiera una cantidad y un monto calculado por su
 * precio y la cantidad.
 * Esta clase realiza el cálculo de manera automática y no necesita una
 * intervención al menos que se requiera lógica extra.
 * @param <D> el objeto completo al que se hará el cálculo.
 */

@Data
@NoArgsConstructor
public class Calculable <D extends ObjetoConPrecio & Irrepetibe<?>> extends Contable<D> {
    @JsonProperty(access = Access.READ_ONLY) protected double monto;

    @JsonGetter("monto")
    public double getMonto() {
        this.monto = detalle.getPrecio() * cantidad;
        return monto;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ObjetoConPrecio objetoConPrecio)
            return detalle.equals(objetoConPrecio);
        return super.equals(obj);
    }
}
