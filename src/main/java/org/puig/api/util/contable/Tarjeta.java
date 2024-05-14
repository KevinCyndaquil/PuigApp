package org.puig.api.util.contable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.puig.api.util.grupos.InitInfo;

@Data
@EqualsAndHashCode(exclude = "banco")
public class Tarjeta {
    @NotBlank(message = "Se requiere el bando de la tarjeta",
            groups = InitInfo.class)
    @Pattern(regexp = "(?U)^[\\p{Lu}\\p{M}\\d]+( [\\p{Lu}\\p{M}\\d]+)*$",
            message = "Nombre de banco invalido",
            groups = InitInfo.class)
    private String banco;
    @NotBlank(message = "Se requiere el número de cuenta de la tarjeta",
            groups = InitInfo.class)
    @Pattern(regexp = "^[0-9]{4} [0-9]{4} [0-9]{4} [0-9]{4}|[0-9]{4} [0-9]{4} [0-9]{4} [0-9]{4} [0-9]{2}$",
            message = "Formato de cuenta es incorrecto",
            groups = InitInfo.class)
    private String numero;
}
