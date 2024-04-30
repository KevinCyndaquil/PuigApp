package org.puig.puigapi.util.contable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.puig.puigapi.util.persistence.Instantiator;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "banco")
public class Tarjeta {
    private String banco;
    private String numero;

    @Data
    @NoArgsConstructor
    public static class Request implements Instantiator<Tarjeta> {
        @NotBlank(message = "Se requiere el bando de la tarjeta")
        @Pattern(regexp = "(?U)^[\\p{Lu}\\p{M}\\d]+( [\\p{Lu}\\p{M}\\d]+)*$",
                message = "Nombre de banco invalido")
        private String banco;
        @NotBlank(message = "Se requiere el número de cuenta de la tarjeta")
        @Pattern(regexp = "^[0-9]{4} [0-9]{4} [0-9]{4} [0-9]{4}|[0-9]{4} [0-9]{4} [0-9]{4} [0-9]{4} [0-9]{2}$",
                message = "Formato de cuenta es incorrecto")
        private String numero;

        @Override
        public Tarjeta instance() {
            return Tarjeta.builder()
                    .banco(banco)
                    .numero(numero)
                    .build();
        }
    }
}
