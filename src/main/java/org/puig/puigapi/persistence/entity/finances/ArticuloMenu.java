package org.puig.puigapi.persistence.entity.finances;

import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.persistence.entity.admin.Producto;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Hashtable;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "finances")
public class ArticuloMenu extends Articulo {
    private @NotNull ArticuloMenu.Categorias categoria;
    private Hashtable<Producto, Float> receta = new Hashtable<>();

    public enum Categorias {
        ADICIONAL,
        PLATILLO,
        BEBIDA
    }
}
