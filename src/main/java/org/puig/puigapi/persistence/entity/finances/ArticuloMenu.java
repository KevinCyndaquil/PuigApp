package org.puig.puigapi.persistence.entity.finances;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.persistence.entity.admin.ProductoTienda;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Hashtable;

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
    @NotNull private Categorias categoria;
    @NotNull private Hashtable<ProductoTienda, Double> receta = new Hashtable<>();

    /**
     * Es el tipo o categoria en la que se puede clasificar un artículo del menú.
     */
    public enum Categorias {
        ADICIONAL,
        PLATILLO,
        BEBIDA
    }
}
