package org.puig.puigapi.persistence.entity.utils;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.puig.puigapi.persistence.entity.utils.persistence.Irrepetibe;

/**
 * Representa una entidad generica que funciona como un detalle.
 * @param <Obj> el objeto que debe ser detallable.
 */

public interface Detallable <Obj extends ObjetoConPrecio & Irrepetibe<?>> {
    @JsonGetter("objeto") Obj getObjeto();
    @JsonSetter("cantidad") int getCantidad();

    /**
     * @return la multiplicación de precio de objeto * cantidad
     */
    @JsonGetter("monto")
    default double getMonto() {
        return getObjeto().getPrecio() * getCantidad();
    }
}
