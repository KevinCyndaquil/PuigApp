package org.puig.puigapi.util;

import com.fasterxml.jackson.annotation.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.puig.puigapi.persistence.entity.admin.Proveedor;
import org.puig.puigapi.persistence.entity.finances.ArticuloMenu;
import org.puig.puigapi.persistence.entity.finances.Combo;
import org.puig.puigapi.persistence.entity.operation.Sucursal;
import org.puig.puigapi.util.contable.Contable;
import org.puig.puigapi.util.contable.Detalle;
import org.puig.puigapi.util.contable.ObjetoConPrecio;
import org.puig.puigapi.util.persistence.Irrepetibe;
import org.puig.puigapi.util.persistence.UniqueName;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

/**
 * Es un árticulo de venta al público génerico, por ejemplo, es un producto que se exhibe en un menú
 * o en su defecto, el que se oferta al usuario.
 * Por estas características, debe contener una descripción
 * con su contenido.
 */

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "especializado"
)

@JsonSubTypes({
        @JsonSubTypes.Type(value = ArticuloMenu.class, name = "ARTICULO_MENU"),
        @JsonSubTypes.Type(value = Combo.class, name = "ARTICULO_COMBO")
})

@Data
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection = "finances")
public abstract class Articulo implements Irrepetibe<String>, ObjetoConPrecio, UniqueName<String> {
    @Id private String id;
    private String nombre;
    private double precio;
    private Especializaciones especializado;
    private boolean visible = true;
    @BsonIgnore protected boolean en_desabasto;

    public enum Especializaciones {
        ARTICULO_MENU,
        ARTICULO_COMBO
    }

    public enum Categorias {
        POLLO,
        ADICIONAL,
        BEBIDA
    }

    @Override
    public String getId() {
        return id;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Articulo articulo)) return false;
        return id.equals(articulo.id) ||
                nombre.equals(articulo.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre);
    }

    @JsonGetter("especializado")
    public abstract Especializaciones getEspecializado();
    /**
     * @return True si al menos uno de sus Ingredientes dentro de receta tienen un stock menor a 5
     */
    public abstract boolean isEn_desabasto(Sucursal sucursal);
    public abstract Detalle<Contable<Proveedor.Producto>> getReceta();
}
