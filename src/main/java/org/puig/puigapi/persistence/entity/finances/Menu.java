package org.puig.puigapi.persistence.entity.finances;

import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.persistence.entity.IMenu;
import org.puig.puigapi.persistence.entity.admin.Producto;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "finances")
public class Menu extends IMenu {
    private @NotNull Menu.Categorias tipo;
    private @NotNull List<Producto.Receta> receta;

    public Menu(String _codigo, 
                String nombre, 
                float monto, 
                @NotNull Menu.Categorias tipo,
                @NotNull List<Producto.Receta> receta) {
        super(_codigo, nombre, monto);
        this.tipo = tipo;
        this.receta = receta;
    }

    public enum Categorias {
        ADICIONAL,
        PLATO_FUERTE,
        BEBIDA
    }
}
