package org.puig.puigapi.persistence.entity.finances;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.NotNull;
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
public class Combo extends Articulo {
    @DBRef private Set<ArticuloMenu> contenido;
    private @NotNull LocalDate inicia;
    private @NotNull LocalDate vigencia;
}
