package org.puig.puigapi.persistence.entity.finances;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.persistence.entity.utils.PostEntity;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.Set;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "finances")
public class Combo extends Articulo implements PostEntity<Combo> {
    @NotNull @DBRef private Set<ArticuloMenu> contenido;
    @NotNull private LocalDate inicia;
    @NotNull private LocalDate vigencia;

    @Override
    public Combo instance() {
        return this;
    }

    @Override
    public Tipo getTipo() {
        return Tipo.COMBO;
    }
}