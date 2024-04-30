package org.puig.puigapi.util.data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RFC {
    @NotBlank(message = "El rfc no puede estar vacío")
    @Pattern(regexp = "^([A-Z]{4}|[A-Z]{3})[0-9]{6}[A-Z0-9]{3}$",
            message = "Formato de RFC de 2004-04-14 no es válido")
    private String rfc;
}
