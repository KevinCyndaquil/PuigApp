package org.puig.puigapi.persistence.entity.utils;

import com.fasterxml.jackson.annotation.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.puig.puigapi.persistence.entity.finances.ArticuloMenu;
import org.puig.puigapi.persistence.entity.finances.Combo;
import org.puig.puigapi.persistence.entity.utils.persistence.Irrepetibe;
import org.springframework.data.annotation.Id;

import java.util.Objects;

/**
 * Es un árticulo de venta al público génerico, por ejemplo, es un producto que se exhibe en un menú
 * o en su defecto, el que se oferta al usuario.
 * Por estas características, debe contener una descripción
 * con su contenido.
 */

@JsonIgnoreProperties(value = { "target", "source", "tipo" })
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "especializado"
)

@JsonSubTypes({
        @JsonSubTypes.Type(value = ArticuloMenu.class, name = "ARTICULO_MENU"),
        @JsonSubTypes.Type(value = Combo.class, name = "COMBO")
})

@Data
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Articulo
        implements Irrepetibe<String>, ObjetoConPrecio {

    @Id private String codigo;
    private String nombre;
    private double precio;
    private Especializaciones especializado;
    private boolean visible = true;

    public enum Especializaciones {
        ARTICULO_MENU,
        COMBO
    }

    @Override
    public String getId() {
        return codigo;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Articulo articulo)) return false;
        return codigo.equals(articulo.codigo) ||
                nombre.equals(articulo.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo, nombre);
    }

    @JsonGetter("especializado")
    public abstract Especializaciones getEspecializado();
}
