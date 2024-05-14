package org.puig.api.util.contable;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.puig.api.util.grupos.InitInfo;
import org.puig.api.util.persistence.Unico;
import org.springframework.data.mongodb.core.mapping.DBRef;

/**
 * Representa un objeto contable cualquiera que a partir de un objeto completo, se necesite
 * administrar varios de ellos.
 * @param <D> el objeto completo a manejar en cantidades.
 */

@Data
@NoArgsConstructor
public class Contable <D extends Unico> {
    @DBRef(lazy = true) protected @Valid D detalle;
    @Positive(groups = InitInfo.class) protected double cantidad;

    public <C extends Contable<D>> Contable(@NonNull C c) {
        this.detalle = c.getDetalle();
        this.cantidad = c.getCantidad();
    }

    public static <D extends Unico> @NonNull Contable<D> of(D detalle, double cantidad) {
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
