package org.puig.puigapi.persistence.entity.utils.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Telefono {
    private String numero;

    @JsonCreator
    public Telefono(
            @NotBlank(message = "Número telefónico no puede estar vacío")
            @Pattern(regexp = "^[0-9]{2} [0-9]{3} [0-9]{3} [0-9]{4}|\\+\\([0-9]{2}\\) [0-9]{3} [0-9]{3} [0-9]{4}|[0-9]{4} [0-9]{4}$",
                    message = "Formato de teléfono no es válido")
            @JsonProperty String numero) {
        this.numero = numero;
    }
}
