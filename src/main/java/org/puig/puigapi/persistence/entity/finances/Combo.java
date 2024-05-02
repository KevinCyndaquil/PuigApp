package org.puig.puigapi.persistence.entity.finances;

import com.fasterxml.jackson.annotation.JsonGetter;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.puig.puigapi.persistence.entity.admin.Proveedor;
import org.puig.puigapi.persistence.entity.operation.Sucursal;
import org.puig.puigapi.util.Articulo;
import org.puig.puigapi.util.contable.Contable;
import org.puig.puigapi.util.contable.Detalle;
import org.puig.puigapi.util.persistence.Instantiator;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.Collection;

/**
 * Conjunto de artículos que se encuentran en el menú que por consiguente traen un descuento
 * o son considerados una promoción.
 */

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "finances")
public class Combo extends Articulo {
    private Contenido contenido;
    private LocalDate inicia;
    private LocalDate vigencia;

    @Override
    public Especializaciones getEspecializado() {
        return Especializaciones.ARTICULO_COMBO;
    }

    @Override
    public boolean isEn_desabasto(Sucursal sucursal) {
        en_desabasto = contenido.stream()
                .map(Contable::getDetalle)
                .map(a -> a.isEn_desabasto(sucursal))
                .reduce(false, Boolean::logicalOr);
        return en_desabasto;
    }

    @Override
    @JsonGetter("contenido")
    public Detalle<Contable<Proveedor.Producto>> getContenido() {
        return contenido.stream()
                .map(Contable::getDetalle)
                .map(ArticuloMenu::getContenido)
                .flatMap(Collection::stream)
                .collect(Detalle::new, Detalle::add, Detalle::addAll);
    }

    @Data
    public static class PostRequest implements Instantiator<Combo> {
        @NotBlank(message = "Código del combo o promoción de menú no válido")
        private String id;
        @NotBlank(message = "Nombre del combo o promoción de menú no válido")
        @Pattern(regexp = "(?U)^[\\p{Lu}\\p{M}\\d]+( [\\p{Lu}\\p{M}\\d]+)*$",
                message = "Formato de nombre incorrecto, recuerda usar solo mayúsculas y dígitos")
        private String nombre;
        @PositiveOrZero(message = "El precio del combo o promición debe ser mayor o igual a cero")
        private double precio;
        private boolean visible = true;
        @NotEmpty(message = "Contenido del combo o promoción no válido")
        private Contenido contenido;
        @NotNull(message = "Se requiere la fecha de inicio del combo")
        private LocalDate inicia;
        private LocalDate vigencia;

        @Override
        public Combo instance() {
            return Combo.builder()
                    .id(id)
                    .nombre(nombre)
                    .precio(precio)
                    .visible(visible)
                    .contenido(contenido)
                    .inicia(inicia)
                    .vigencia(vigencia)
                    .build();
        }
    }

    public static class Contenido extends Detalle<Contable<ArticuloMenu>> {

    }
}