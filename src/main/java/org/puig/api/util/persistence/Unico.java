package org.puig.api.util.persistence;

import org.bson.types.ObjectId;

/**
 * Representa una clase en donde cada instancia de ella debe ser unica.
 */
public interface Unico {
    /**
     * @return id del objeto.
     */
    ObjectId getId();
    void setId(ObjectId id);
}
