package org.puig.puigapi.persistence.entity.finances;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.persistence.entity.utils.Irrepetibe;
import org.puig.puigapi.persistence.entity.utils.ObjetoConPrecio;
import org.springframework.data.annotation.Id;

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
@EqualsAndHashCode(exclude = {"precio", "tipo"})
public abstract class Articulo
        implements Irrepetibe<String>, ObjetoConPrecio {

    @Id private String codigo;
    @NotNull private String nombre;
    private double precio;
    @NotNull private Tipo tipo = Tipo.ARTICULO_MENU;

    public enum Tipo {
        ARTICULO_MENU,
        COMBO
    }

    @Override
    public String getId() {
        return codigo;
    }
}
