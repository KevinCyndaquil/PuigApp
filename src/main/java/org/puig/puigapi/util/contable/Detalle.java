package org.puig.puigapi.util.contable;

import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@NoArgsConstructor
public class Detalle <D extends Contable<?>> extends HashSet<D> {

    public <C extends Collection<D>> Detalle(C c) {
        super(c);
    }

    /**
     * Multiplica la cantidad de cada receta por otra. Se utiliza cuando se tienen varias cantidades
     * de la misma receta, y se requiere obtener el total de cantidades según el total de articulos.
     * @param cantidad la cantidad de artíclos que se tiene.
     * @return un Stream con cada ingrediente teniendo su cantidad multiplicada por la cantidad de
     * artículos dados.
     */
    public Stream<D> per(double cantidad) {
        return stream()
                .peek(d -> d.per(cantidad));
    }

    public Set<?> asDetalle() {
        return stream()
                .map(Contable::getDetalle)
                .collect(Collectors.toSet());
    }
}