package org.puig.puigapi.util.contable;

import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.util.persistence.Irrepetibe;
import org.springframework.data.mongodb.core.mapping.DBRef;

/**
 * Representa un objeto contable cualquiera que a partir de un objeto completo, se necesite
 * administrar varios de ellos.
 * @param <D> el objeto completo a manejar en cantidades.
 */

@Data
public class Contable <D extends Irrepetibe<?>> {
    @DBRef(lazy = true) protected D detalle;
    @Positive protected double cantidad;

    public static <D extends Irrepetibe<?>> @NotNull Contable<D> of(D detalle, double cantidad) {
        Contable<D> contable = new Contable<>();
        contable.setDetalle(detalle);
        contable.setCantidad(cantidad);
        return contable;
    }

    public Contable<D> plus(double cantidad) {
        this.cantidad += cantidad;
        return this;
    }
    public Contable<D> minus(double cantidad) {
        this.cantidad -= cantidad;
        return this;
    }
    public Contable<D> per(double cantidad) {
        this.cantidad *= cantidad;
        return this;
    }

    public boolean equals(D d) {
        return detalle.equals(d);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Contable<?> contable)
            return detalle.equals(contable.detalle);
        return false;
    }

    @Override
    public int hashCode() {
        return detalle.hashCode();
    }
}
