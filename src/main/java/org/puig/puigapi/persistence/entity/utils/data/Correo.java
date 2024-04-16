package org.puig.puigapi.persistence.entity.utils.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Correo {
    private String direccion;

    @JsonCreator
    public Correo(
            @NotBlank(message = "Direccion de correo electrónico no puede estar vacío")
            @Pattern(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$",
                    message = "Correo electrónico de proveedor no es válido")
            String direccion) {
        this.direccion = direccion;
    }
}
