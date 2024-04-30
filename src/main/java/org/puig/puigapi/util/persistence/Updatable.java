package org.puig.puigapi.util.persistence;

/**
 * Se utiliza cuando una entidad necesita ser actualizada antes de persistirse.
 */
public interface Updatable {
    void update();
}
