package org.puig.puigapi.util.data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Curp {
    @NotBlank(message = "Curp no puede estar vacía")
    @Pattern(regexp = "^[A-Z]{4}[0-9]{6}[HM][A-Z]{2}[A-Z]{3}[0-9A-Z][0-9]$",
            message = "Formato de curp no es válida")
    private String curp;
}
