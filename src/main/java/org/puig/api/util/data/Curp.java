package org.puig.api.util.data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.puig.api.util.grupos.InitInfo;

@Data
@NoArgsConstructor
public class Curp {
    @NotBlank(message = "Curp no puede estar vacía"
            , groups = InitInfo.class)
    @Pattern(regexp = "^[A-Z]{4}[0-9]{6}[HM][A-Z]{2}[A-Z]{3}[0-9A-Z][0-9]$",
            message = "Formato de curp no es válida",
            groups = InitInfo.class)
    private String curp;
}
