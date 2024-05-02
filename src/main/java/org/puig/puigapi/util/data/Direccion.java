package org.puig.puigapi.util.data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.puig.puigapi.util.persistence.Instantiator;

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
    private String edificio;
    private String lote;
    private String numero_interior;
    private String descripcion_vivienda;

    /**
     * Se utiliza cuando la dirección se requiere para realizar una factura
     */
    @Data
    @NoArgsConstructor
    public static class ParaFactura implements Instantiator<Direccion> {
        @Pattern(regexp = "(?U)^[\\p{Lu}\\p{M}\\d]+( [\\p{Lu}\\p{M}\\d]+)*$",
                message = "Formato para nombre de municipio invalido")
        private String municipio;
        @Pattern(regexp = "(?U)^[\\p{Lu}\\p{M}\\d]+( [\\p{Lu}\\p{M}\\d]+)*$",
                message = "Formato para nombre de colonia invalida")
        private String colonia;
        @Pattern(regexp = "^(?!00)[0-9]{2}[0-9]{3}$")
        private String codigo_postal;
        @NotBlank(message = "Se requiere una calle principal para la dirección")
        @Pattern(regexp = "(?U)^[\\p{Lu}\\p{M}\\d]+( [\\p{Lu}\\p{M}\\d]+)*$",
                message = "Formato para nombre de una calle invalida")
        private String calle_principal;
        @Pattern(regexp = "(?U)^[\\p{Lu}\\p{M}\\d]+( [\\p{Lu}\\p{M}\\d]+)*$",
                message = "Formato para nombre de una calle invalida")
        private String calle_1;
        @Pattern(regexp = "(?U)^[\\p{Lu}\\p{M}\\d]+( [\\p{Lu}\\p{M}\\d]+)*$",
                message = "Formato para nombre de una calle invalida")
        private String calle_2;
        @Pattern(regexp = "^[A-Z0-9]+$",
                message = "Formato de nombre de una manzana invalida")
        private String manzana;
        @Pattern(regexp = "^[A-Z0-9]+$",
                message = "Formato de nombre de un edificio invalida")
        private String edificio;
        @NotBlank(message = "Se requiere el lote para la dirección")
        @Pattern(regexp = "^[A-Z0-9]+$",
                message = "Formato de nombre para lote invalida")
        private String lote;
        @Pattern(regexp = "^[A-Z0-9]+$",
                message = "Formato de número interior invalido")
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

    /**
     * Se utiliza cuando la dirección se usará para realizar una describir una ubicación no
     * tan descriptiva.
     * <br>
     * Campos Obligatorios:
     * <ul>
     *     <li>colonia</li>
     *     <li>calle_principal</li>
     * </ul>
     */
    @Data
    @NoArgsConstructor
    public static class PostRequest implements Instantiator<Direccion> {
        @Pattern(regexp = "(?U)^[\\p{Lu}\\p{M}\\d]+( [\\p{Lu}\\p{M}\\d]+)*$",
                message = "Formato para nombre de municipio invalido")
        private String municipio;
        @NotBlank(message = "Se requiere una colonia para la dirección")
        @Pattern(regexp = "(?U)^[\\p{Lu}\\p{M}\\d]+( [\\p{Lu}\\p{M}\\d]+)*$",
                message = "Formato para nombre de colonia invalida")
        private String colonia;
        @Pattern(regexp = "^(?!00)[0-9]{2}[0-9]{3}$")
        private String codigo_postal;
        @NotBlank(message = "Se requiere una calle_principal para la dirección")
        @Pattern(regexp = "(?U)^[\\p{Lu}\\p{M}\\d]+( [\\p{Lu}\\p{M}\\d]+)*$",
                message = "Formato para nombre de calle invalida")
        private String calle_principal;
        @Pattern(regexp = "(?U)^[\\p{Lu}\\p{M}\\d]+( [\\p{Lu}\\p{M}\\d]+)*$",
                message = "Formato para nombre de calle invalida")
        private String calle_1;
        @Pattern(regexp = "(?U)^[\\p{Lu}\\p{M}\\d]+( [\\p{Lu}\\p{M}\\d]+)*$",
                message = "Formato para nombre de calle invalida")
        private String calle_2;
        @Pattern(regexp = "^[A-Z0-9]+$",
                message = "Formato para nombre de manzana invalida")
        private String manzana;
        @Pattern(regexp = "^[A-Z0-9]+$",
                message = "Formato de nombre de un edificio invalida")
        private String edificio;
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
