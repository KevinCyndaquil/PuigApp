package org.puig.puigapi.persistence.entity.operation;

import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.persistence.entity.Direccion;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "ubicacion")
@Document(collection = "finances")
public class Sucursal {
    @Id private String _id;
    private @NotNull String nombre;
    private @NotNull Direccion ubicacion;
}
