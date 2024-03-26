package org.puig.puigapi.persistence.entity.utils;

import lombok.*;
import org.jetbrains.annotations.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "descripcion_vivienda")
public class Direccion {
    private @NotNull String calle_principal;
    private String calle_1;
    private String calle_2;
    private String manzana;
    private String lote;
    private String numero_exterior;
    private @NotNull String numero_interior;
    private @NotNull String descripcion_vivienda;
}
