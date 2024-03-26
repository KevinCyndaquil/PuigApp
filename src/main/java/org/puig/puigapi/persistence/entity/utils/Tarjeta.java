package org.puig.puigapi.persistence.entity.utils;

import lombok.*;
import org.jetbrains.annotations.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "banco")
public class Tarjeta {
    private @NotNull String banco;
    private @NotNull String numero;
}
