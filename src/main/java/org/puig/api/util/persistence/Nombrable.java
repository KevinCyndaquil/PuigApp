package org.puig.api.util.persistence;

import lombok.NonNull;

public interface Nombrable {
    @NonNull String getNombre();
    void setNombre(@NonNull String nombre);
}