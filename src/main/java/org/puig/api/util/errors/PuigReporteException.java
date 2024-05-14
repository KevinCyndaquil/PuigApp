package org.puig.api.util.errors;

import lombok.NonNull;

import java.net.URL;

public class PuigReporteException extends RuntimeException {

    public PuigReporteException(@NonNull URL url) {
        super("Error durante la exportación del reporte en la dirección física %s"
                .formatted(url));
    }
}
