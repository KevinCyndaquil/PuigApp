package org.puig.puigapi.util.persistence;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Representa una clase que sirve unicamente como intermediario entre la APIRest y su creación en
 * la base de datos, conteniendo unicamente los atributos necesarios para su instancía.
 * @param <E> la clase a la que pertenece este Instantiator.
 */
public interface Instantiator<E>{

    /**
     * @return Transforma este objeto a un tipo de persistencia de la base de datos.
     */
    E instance();

    /**
     * Si el instantiator es null, devolverá null, de lo contrarío, lo instanciará.
     * @param instantiator el objeto instantiator a instanciar.
     * @return el objeto dentro de Instantiator instanciado.
     * @param <C> la clase base a instanciar.
     */
    static <C> @Nullable C nullOrGet(Instantiator<C> instantiator) {
        if (Objects.isNull(instantiator)) return null;
        return instantiator.instance();
    }
}
