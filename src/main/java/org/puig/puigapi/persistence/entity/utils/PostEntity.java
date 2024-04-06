package org.puig.puigapi.persistence.entity.utils;

/**
 * Representa una clase que sirve unicamente como intermediario entre la APIRest y su creación en
 * la base de datos, conteniendo unicamente los atributos necesarios para su instancía.
 * @param <E> la clase a la que pertenece este PostEntity.
 */
public interface PostEntity <E extends Irrepetibe<?>>{

    /**
     * @return este objeto Post Entity a su objeto de real interes, aquel que se va a guardar
     * en la base de datos.
     */
    E instance();
}
