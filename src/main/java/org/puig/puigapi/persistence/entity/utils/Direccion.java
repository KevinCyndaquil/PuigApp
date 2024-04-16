package org.puig.puigapi.persistence.entity.utils;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.puig.puigapi.persistence.entity.utils.persistence.PostEntity;

/**
 * Representa una dirección genérica.
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "descripcion_vivienda")
public class Direccion {
    private String municipio;
    private String colonia;
    private String codigo_postal;
    private String calle_principal;
    private String calle_1;
    private String calle_2;
    private String manzana;
    private String lote;
    private String numero_interior;
    private String descripcion_vivienda;

    @Data
    @NoArgsConstructor
    public static class RequestFacturacion implements PostEntity<Direccion> {
        @Pattern(regexp = "(?U)^[\\p{Lu}\\p{M}\\d]+( [\\p{Lu}\\p{M}\\d]+)*$",
                message = "Formato para nombres de municipio invalido")
        private String municipio;
        @NotBlank(message = "Colonia en dirección no puede estar vacía")
        @Pattern(regexp = "(?U)^[\\p{Lu}\\p{M}\\d]+( [\\p{Lu}\\p{M}\\d]+)*$",
                message = "Formato para nombres de colonia invalida")
        private String colonia;
        @NotBlank(message = "Código postal no puede estar vacío")
        @Pattern(regexp = "^(?!00)[0-9]{2}[0-9]{3}$")
        private String codigo_postal;
        @NotBlank(message = "Municio en dirección no puede estar vacía")
        @Pattern(regexp = "(?U)^[\\p{Lu}\\p{M}\\d]+( [\\p{Lu}\\p{M}\\d]+)*$",
                message = "Formato de nombre para una calle principal invalida")
        private String calle_principal;
        @Pattern(regexp = "(?U)^[\\p{Lu}\\p{M}\\d]+( [\\p{Lu}\\p{M}\\d]+)*$",
                message = "Formato de nombre para una calle 1 invalida")
        private String calle_1;
        @Pattern(regexp = "(?U)^[\\p{Lu}\\p{M}\\d]+( [\\p{Lu}\\p{M}\\d]+)*$",
                message = "Formato de nombre para una calle 2 invalida")
        private String calle_2;
        @Pattern(regexp = "^[A-Z0-9]+$",
                message = "Formato de nombre de manzana invalida")
        private String manzana;
        @NotBlank(message = "Lote de dirección no puede estar vacío")
        @Pattern(regexp = "^[A-Z0-9]+$", message = "Formato de nombre para lote invalida")
        private String lote;
        @Pattern(regexp = "^[A-Z0-9]+$", message = "Formato de número interior invalido")
        private String numero_interior;
        private String descripcion_vivienda;

        @Override
        public Direccion instance() {
            return Direccion.builder()
                    .municipio(municipio)
                    .colonia(colonia)
                    .codigo_postal(codigo_postal)
                    .calle_principal(calle_principal)
                    .calle_1(calle_1)
                    .calle_2(calle_2)
                    .manzana(manzana)
                    .lote(lote)
                    .numero_interior(numero_interior)
                    .descripcion_vivienda(descripcion_vivienda)
                    .build();
        }
    }

    @Data
    @NoArgsConstructor
    public static class RequestUsuario implements PostEntity<Direccion> {
        @Pattern(regexp = "(?U)^[\\p{Lu}\\p{M}\\d]+( [\\p{Lu}\\p{M}\\d]+)*$",
                message = "Formato para nombres de municipio invalido")
        private String municipio;
        @NotBlank(message = "Colonia en dirección no puede estar vacía")
        @Pattern(regexp = "(?U)^[\\p{Lu}\\p{M}\\d]+( [\\p{Lu}\\p{M}\\d]+)*$",
                message = "Formato para nombres de colonia invalida")
        private String colonia;
        @Pattern(regexp = "^(?!00)[0-9]{2}[0-9]{3}$")
        private String codigo_postal;
        @NotBlank(message = "Municio en dirección no puede estar vacía")
        @Pattern(regexp = "(?U)^[\\p{Lu}\\p{M}\\d]+( [\\p{Lu}\\p{M}\\d]+)*$",
                message = "Formato de nombre para una calle principal invalida")
        private String calle_principal;
        @Pattern(regexp = "(?U)^[\\p{Lu}\\p{M}\\d]+( [\\p{Lu}\\p{M}\\d]+)*$",
                message = "Formato de nombre para una calle 1 invalida")
        private String calle_1;
        @Pattern(regexp = "(?U)^[\\p{Lu}\\p{M}\\d]+( [\\p{Lu}\\p{M}\\d]+)*$",
                message = "Formato de nombre para una calle 2 invalida")
        private String calle_2;
        @Pattern(regexp = "^[A-Z0-9]+$",
                message = "Formato de nombre de manzana invalida")
        private String manzana;
        @Pattern(regexp = "^[A-Z0-9]+$", message = "Formato de nombre para lote invalida")
        private String lote;
        @Pattern(regexp = "^[A-Z0-9]+$", message = "Formato de número interior invalido")
        private String numero_interior;
        private String descripcion_vivienda;

        @Override
        public Direccion instance() {
            return Direccion.builder()
                    .municipio(municipio)
                    .colonia(colonia)
                    .codigo_postal(codigo_postal)
                    .calle_principal(calle_principal)
                    .calle_1(calle_1)
                    .calle_2(calle_2)
                    .manzana(manzana)
                    .lote(lote)
                    .numero_interior(numero_interior)
                    .descripcion_vivienda(descripcion_vivienda)
                    .build();
        }
    }
}
