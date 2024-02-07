package org.puig.puigapi.persistence.entity.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "banco")
public class Tarjeta {
    private @NotNull String banco;
    private @NotNull String numero;
}
