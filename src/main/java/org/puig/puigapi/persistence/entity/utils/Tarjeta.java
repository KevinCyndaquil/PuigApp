package org.puig.puigapi.persistence.entity.utils;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.puig.puigapi.persistence.entity.utils.persistence.PostEntity;

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
    public static class Request implements PostEntity<Tarjeta> {
        @NotBlank(message = "Banco de la tarjeta no puede estar vacío")
        @Pattern(regexp = "^[A-Z]+$",
                message = "Nombre de banco invalido")
        private String banco;
        @NotBlank(message = "Número de la tarjeta no puede estar vacío")
        @Pattern(regexp = "^[0-9]{4} [0-9]{4} [0-9]{4} [0-9]{4}$",
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
