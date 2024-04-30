package org.puig.puigapi.util.persistence;

/**
 * Representa una clase en donde cada instancia de ella debe ser unica.
 * @param <ID> el especializado de dato del ID o identificador.
 */
public interface Irrepetibe<ID> {
    /**
     * @return id del objeto.
     */
    ID getId();
    void setId(ID id);
}
