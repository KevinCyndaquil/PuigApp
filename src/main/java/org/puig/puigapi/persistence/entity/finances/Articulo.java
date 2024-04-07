package org.puig.puigapi.persistence.entity.finances;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.puig.puigapi.persistence.entity.utils.Irrepetibe;
import org.puig.puigapi.persistence.entity.utils.ObjetoConPrecio;
import org.springframework.data.annotation.Id;

import java.util.Objects;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "tipo"
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
    private Tipo tipo;

    public enum Tipo {
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

    public abstract Tipo getTipo();
}
