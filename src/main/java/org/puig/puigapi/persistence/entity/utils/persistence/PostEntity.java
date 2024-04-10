package org.puig.puigapi.persistence.entity.utils.persistence;

/**
 * Representa una clase que sirve unicamente como intermediario entre la APIRest y su creación en
 * la base de datos, conteniendo unicamente los atributos necesarios para su instancía.
 * @param <E> la clase a la que pertenece este PostEntity.
 */
public interface PostEntity <E>{

    /**
     * @return este objeto Request Entity a su objeto de real interes, aquel que se va a guardar
     * en la base de datos.
     */
    E instance();
}
