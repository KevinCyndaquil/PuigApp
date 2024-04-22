package org.puig.puigapi.exceptions;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

@Getter
public class BusquedaSinResultadoException extends RuntimeException {
    private final String param;
    private final Object value;

    public BusquedaSinResultadoException(@NotNull Class<?> clazzFound,
                                         String paramName,
                                         Object paramValue) {
        super("No se encontraron %s para %s con valor %s"
                .formatted(clazzFound.getSimpleName(), paramName, paramValue));
        this.param = paramName;
        this.value = paramValue;
    }

    public BusquedaSinResultadoException(String paramName,
                                         Object paramValue) {
        super("No se encontraron resultados para %s con valor %s"
                .formatted(paramName, paramValue));
        this.param = paramName;
        this.value = paramValue;
    }

    public BusquedaSinResultadoException(@NotNull Class<?> _class, String[] params, Object[] values) {
        super("No se encontraron %s para resultados para %s con valor %s"
                .formatted(_class.getSimpleName(), params, values));
        this.param = Arrays.toString(params);
        this.value = values;
    }
}
