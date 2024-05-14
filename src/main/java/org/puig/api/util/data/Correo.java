package org.puig.api.util.data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.puig.api.util.grupos.InitInfo;

@Data
@NoArgsConstructor
public class Correo {
    @NotBlank(message = "Direccion de correo electrónico no puede estar vacío",
            groups = InitInfo.class)
    @Pattern(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$",
            message = "Correo electrónico de proveedor no es válido",
            groups = InitInfo.class)
    private String direccion;
}
