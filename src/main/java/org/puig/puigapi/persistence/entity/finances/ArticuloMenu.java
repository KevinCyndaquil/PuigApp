package org.puig.puigapi.persistence.entity.finances;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.persistence.entity.admin.ProductoTienda;
import org.puig.puigapi.persistence.entity.utils.PostEntity;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

/**
 * Representa un producto que se encuentra en el menú. Producto o artículo con precio.
 */

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "finances")
public class ArticuloMenu extends Articulo {
    private Categorias categoria;
    private Set<Receta> receta;

    /**
     * Es el tipo o categoria en la que se puede clasificar un artículo del menú.
     */
    public enum Categorias {
        ADICIONAL,
        PLATILLO,
        BEBIDA
    }

    @Override
    public Tipo getTipo() {
        return Tipo.ARTICULO_MENU;
    }

    public record Post(
            @NotBlank(message = "Código del artículo de menú no válido")
            String codigo,
            @NotBlank(message = "Nombre del artículo de menú no válido")
            String nombre,
            @Positive(message = "El precio del articulo debe ser mayor a cero")
            double precio,
            @NotNull Categorias categoria,
            @NotEmpty Set<Receta> receta
    ) implements PostEntity<ArticuloMenu> {

        @Override
        public ArticuloMenu instance() {
            return ArticuloMenu.builder()
                    .codigo(codigo)
                    .nombre(nombre)
                    .precio(precio)
                    .categoria(categoria)
                    .receta(receta)
                    .tipo(Tipo.ARTICULO_MENU)
                    .build();
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(exclude = "precio")
    public static class Receta {
        @DBRef private ProductoTienda producto;
        @PositiveOrZero(message = "Cantidad de receta de un artículo de menu debe ser mayor o igual a cero")
        private double cantidad;
    }
}
