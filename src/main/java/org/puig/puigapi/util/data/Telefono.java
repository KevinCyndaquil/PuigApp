package org.puig.puigapi.util.data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Telefono {
    @NotBlank(message = "Número de teléfono vacío")
    @Pattern(regexp = "^\\+\\([0-9]{2}\\) [0-9]{3} [0-9]{3} [0-9]{4}|[0-9]{4} [0-9]{4}$",
            message = "Formato de número de teléfono no es válido")
    private String numero;
}
