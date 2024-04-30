package org.puig.puigapi.util.persistence;

import jakarta.validation.constraints.NotNull;

public interface UniqueName <N> {
    @NotNull N getNombre();
    void setNombre(@NotNull N n);
}