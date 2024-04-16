package org.puig.puigapi.persistence.entity.utils.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RFC {
    private String rfc;

    @JsonCreator
    public RFC(
            @NotBlank(message = "El rfc no puede estar vacío")
            @Pattern(regexp = "^[A-Z]{4}[0-9]{6}[A-Z0-9]{3}$",
                    message = "Formato de RFC de 2004-04-14 no es válido")
            String rfc) {
        this.rfc = rfc;
    }
}
