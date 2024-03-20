package org.puig.puigapi.persistence.entity.finances;

import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "finances")
public class Combo extends Articulo {
    @DBRef private Set<ArticuloMenu> contenido;
    private @NotNull LocalDate inicia;
    private @NotNull LocalDate vigencia;

    public Combo(String _codigo,
                 String nombre,
                 float monto,
                 Set<ArticuloMenu> contenido,
                 @NotNull LocalDate inicia,
                 @NotNull LocalDate vigencia) {
        super(_codigo, nombre, monto);
        this.contenido = contenido;
        this.inicia = inicia;
        this.vigencia = vigencia;
    }
}
