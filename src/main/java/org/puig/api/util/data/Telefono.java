package org.puig.api.util.data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.puig.api.util.grupos.InitInfo;
import org.puig.api.util.grupos.UniqueInfo;

@Data
@NoArgsConstructor
public class Telefono {
    @NotBlank(message = "Número de teléfono vacío",
            groups = {InitInfo.class, UniqueInfo.class})
    @Pattern(regexp = "^\\+\\([0-9]{2}\\) [0-9]{3} [0-9]{3} [0-9]{4}|[0-9]{4} [0-9]{4}$",
            message = "Formato de número de teléfono no es válido",
            groups = {InitInfo.class, UniqueInfo.class})
    private String numero;
}
