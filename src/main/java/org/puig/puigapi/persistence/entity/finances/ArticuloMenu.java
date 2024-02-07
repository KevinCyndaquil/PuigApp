package org.puig.puigapi.persistence.entity.finances;

import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.persistence.entity.admin.Producto;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "finances")
public class ArticuloMenu extends Articulo {
    private @NotNull ArticuloMenu.Categorias tipo;
    private @NotNull Map<Producto, Float> receta;

    public ArticuloMenu(String _codigo,
                        String nombre,
                        float monto,
                        @NotNull ArticuloMenu.Categorias tipo,
                        @NotNull Map<Producto, Float> receta) {
        super(_codigo, nombre, monto);
        this.tipo = tipo;
        this.receta = receta;
    }

    public enum Categorias {
        ADICIONAL,
        PLATILLO,
        BEBIDA
    }
}
