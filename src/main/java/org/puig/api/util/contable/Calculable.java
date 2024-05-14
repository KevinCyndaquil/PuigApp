package org.puig.api.util.contable;

import com.fasterxml.jackson.annotation.JsonGetter;
import jakarta.validation.constraints.Positive;
import org.puig.api.util.grupos.InitInfo;
import org.puig.api.util.persistence.Unico;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Representa cualquier objeto completo que requiera una cantidad y un monto calculado por su
 * precio y la cantidad.
 * Esta clase realiza el cálculo de manera automática y no necesita una
 * intervención al menos que se requiera lógica extra.
 * @param <D> el objeto completo al que se hará el cálculo.
 */

public class Calculable <D extends ObjetoConPrecio & Unico> extends Contable<D> {

    @Field("monto")
    @JsonGetter("monto")
    @Positive(groups = InitInfo.class)
    public double getMonto() {
        return detalle.getPrecio() * cantidad;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ObjetoConPrecio objetoConPrecio)
            return detalle.equals(objetoConPrecio);
        return super.equals(obj);
    }
}
