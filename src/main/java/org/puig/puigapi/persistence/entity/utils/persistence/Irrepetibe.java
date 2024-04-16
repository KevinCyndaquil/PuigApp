package org.puig.puigapi.persistence.entity.utils.persistence;

/**
 * Representa una clase en donde cada instancia de ella debe ser unica.
 * @param <ID> el especializado de dato del id o identificador.
 */
public interface Irrepetibe<ID> {
    /**
     * @return id del objeto.
     */
    ID getId();
}
