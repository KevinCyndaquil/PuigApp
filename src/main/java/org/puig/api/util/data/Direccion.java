package org.puig.api.util.data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.puig.api.util.grupos.FacturaInfo;
import org.puig.api.util.grupos.InitInfo;

/**
 * Representa una dirección genérica.
 */

@Data
@EqualsAndHashCode(exclude = "descripcion_vivienda")
public class Direccion {
    @Pattern(regexp = "(?U)^[\\p{Lu}\\p{M}\\d]+( [\\p{Lu}\\p{M}\\d]+)*$",
            message = "Formato para nombre de municipio invalido",
            groups = {InitInfo.class, FacturaInfo.class})
    private String municipio;
    @NotBlank(message = "Se requiere la colonia para la dirección",
            groups = InitInfo.class)
    @Pattern(regexp = "(?U)^[\\p{Lu}\\p{M}\\d]+( [\\p{Lu}\\p{M}\\d]+)*$",
            message = "Formato para nombre de colonia invalida",
            groups = {InitInfo.class, FacturaInfo.class})
    private String colonia;
    @Pattern(regexp = "^(?!00)[0-9]{2}[0-9]{3}$",
            message = "Formato de código postal invalido",
            groups = {InitInfo.class, FacturaInfo.class})
    private String codigo_postal;
    @NotBlank(message = "Se requiere una calle principal para la dirección",
            groups = {InitInfo.class, FacturaInfo.class})
    @Pattern(regexp = "(?U)^[\\p{Lu}\\p{M}\\d]+( [\\p{Lu}\\p{M}\\d]+)*$",
            message = "Formato para nombre de una calle invalida")
    private String calle_principal;
    @Pattern(regexp = "(?U)^[\\p{Lu}\\p{M}\\d]+( [\\p{Lu}\\p{M}\\d]+)*$",
            message = "Formato para nombre de una calle invalida",
            groups = {InitInfo.class, FacturaInfo.class})
    private String calle_1;
    @Pattern(regexp = "(?U)^[\\p{Lu}\\p{M}\\d]+( [\\p{Lu}\\p{M}\\d]+)*$",
            message = "Formato para nombre de una calle invalida",
            groups = {InitInfo.class, FacturaInfo.class})
    private String calle_2;
    @Pattern(regexp = "^[A-Z0-9]+$",
            message = "Formato de nombre de una manzana invalida",
            groups = {InitInfo.class, FacturaInfo.class})
    private String manzana;
    @Pattern(regexp = "^[A-Z0-9]+$",
            message = "Formato de nombre de un edificio invalida",
            groups = {InitInfo.class, FacturaInfo.class})
    private String edificio;
    @NotBlank(message = "Se requiere el lote para la dirección",
            groups = FacturaInfo.class)
    @Pattern(regexp = "^[A-Z0-9]+$",
            message = "Formato de nombre para lote invalida",
            groups = {InitInfo.class, FacturaInfo.class})
    private String lote;
    @Pattern(regexp = "^[A-Z0-9]+$",
            message = "Formato de número interior invalido",
            groups = {InitInfo.class, FacturaInfo.class})
    private String numero_interior;
    private String descripcion_vivienda;
}
