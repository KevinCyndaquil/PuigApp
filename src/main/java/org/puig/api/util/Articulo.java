package org.puig.api.util;

import com.fasterxml.jackson.annotation.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.bson.types.ObjectId;
import org.puig.api.persistence.entity.admin.Proveedor;
import org.puig.api.persistence.entity.finances.ArticuloMenu;
import org.puig.api.persistence.entity.finances.Combo;
import org.puig.api.persistence.entity.operation.Sucursal;
import org.puig.api.util.contable.Contable;
import org.puig.api.util.contable.Detalle;
import org.puig.api.util.contable.ObjetoConPrecio;
import org.puig.api.util.grupos.InitInfo;
import org.puig.api.util.grupos.UniqueInfo;
import org.puig.api.util.persistence.Unico;
import org.puig.api.util.persistence.Nombrable;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Objects;

/**
 * Es un árticulo de venta al público génerico, por ejemplo, es un producto que se exhibe en un menú
 * o en su defecto, el que se oferta al usuario.
 * Por estas características, debe contener una descripción
 * con su contenido.
 */

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "especializado")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ArticuloMenu.class, name = "ARTICULO_MENU"),
        @JsonSubTypes.Type(value = Combo.class, name = "ARTICULO_COMBO")})

@Data
@Document(collection = "finances")
public abstract class Articulo implements Unico, ObjetoConPrecio, Nombrable {
    @Null(groups = InitInfo.class) @NotNull(groups = UniqueInfo.class)
    private ObjectId id;
    @NotBlank(message = "Se requiere el código del artículo",
            groups = InitInfo.class)
    @Pattern(regexp = "^[A-Z0-9]+$",
            message = "Utiliza códigos con solo mayúsculas y números",
            groups = InitInfo.class)
    private String codigo;
    @NotBlank(message = "Nombre del artículo no válido",
            groups = InitInfo.class)
    @Pattern(regexp = "(?U)^[\\p{Lu}\\p{M}\\d]+( [\\p{Lu}\\p{M}\\d]+)*$",
            message = "Formato de nombre incorrecto, recuerda usar solo mayúsculas y dígitos",
            groups = InitInfo.class)
    private String nombre;
    @Positive(message = "El precio del articulo debe ser mayor a cero",
            groups = InitInfo.class)
    private double precio;
    private boolean visible = true;
    protected boolean en_desabasto;

    @Field("especializado")
    @JsonGetter("especializado")
    public abstract Especializaciones getEspecializado();

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
    public boolean equals(Object obj) {
        if (!(obj instanceof Articulo articulo)) return false;
        return id.equals(articulo.id) ||
                nombre.equals(articulo.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre);
    }

    /**
     * @return True si al menos uno de sus Ingredientes dentro de receta tienen un stock menor a 5
     */
    public abstract boolean isEn_desabasto(Sucursal sucursal);
    public abstract Detalle<Contable<Proveedor.Producto>> getContables();
}
