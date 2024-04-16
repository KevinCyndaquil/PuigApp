package org.puig.puigapi.persistence.entity.utils.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Curp {
    private String curp;

    @JsonCreator
    public Curp(
            @NotBlank(message = "Curp no puede estar vacía")
            @Pattern(regexp = "^[A-Z]{4}[0-9]{6}[HM][A-Z]{2}[A-Z]{3}[0-9A-Z][0-9]$",
                    message = "Formato de curp no es válida")
            String curp) {
        this.curp = curp;
    }
}
