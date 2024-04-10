package org.puig.puigapi.persistence.entity.utils;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.puig.puigapi.persistence.entity.utils.persistence.PostEntity;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "descripcion_vivienda")
public class Direccion {
    private String municipio;
    private String colonia;
    private String calle_principal;
    private String calle_1;
    private String calle_2;
    private String manzana;
    private String lote;
    private String numero_exterior;
    private String numero_interior;
    private String descripcion_vivienda;

    @Data
    @NoArgsConstructor
    public static class Request implements PostEntity<Direccion> {
        @NotBlank(message = "Municio en dirección no puede estar vacía")
        @Pattern(regexp = "^[A-Z]+(?: [A-Z]+)*$", message = "Municipio invalido")
        private String municipio;
        @NotBlank(message = "Colonia en dirección no puede estar vacía")
        @Pattern(regexp = "^[A-Z0-9]+(?: [A-Z0-9]+)*$", message = "Colonia invalida")
        private String colonia;
        @NotBlank(message = "Municio en dirección no puede estar vacía")
        @Pattern(regexp = "^[A-Z0-9]+(?: [A-Z0-9]+)*$", message = "Calle principal invalida")
        private String calle_principal;
        @Pattern(regexp = "^[A-Z0-9]+(?: [A-Z0-9]+)*$", message = "Calle 1 invalida")
        private String calle_1;
        @Pattern(regexp = "^[A-Z0-9]+(?: [A-Z0-9]+)*$", message = "Calle 2 invalida")
        private String calle_2;
        @Pattern(regexp = "^[A-Z0-9]+$", message = "Calle 2 invalida")
        private String manzana;
        @Pattern(regexp = "^[A-Z0-9]+$", message = "Lote invalido")
        private String lote;
        @Pattern(regexp = "^[A-Z0-9]+$", message = "Número exterior invalido")
        private String numero_exterior;
        @NotBlank(message = "Municio en dirección no puede estar vacía")
        @Pattern(regexp = "^[A-Z0-9]+$", message = "Número interior invalido")
        private String numero_interior;
        private String descripcion_vivienda;

        @Override
        public Direccion instance() {
            return Direccion.builder()
                    .municipio(municipio)
                    .colonia(colonia)
                    .calle_principal(calle_principal)
                    .calle_1(calle_1)
                    .calle_2(calle_2)
                    .manzana(manzana)
                    .lote(lote)
                    .numero_interior(numero_interior)
                    .numero_exterior(numero_exterior)
                    .descripcion_vivienda(descripcion_vivienda)
                    .build();
        }
    }
}
