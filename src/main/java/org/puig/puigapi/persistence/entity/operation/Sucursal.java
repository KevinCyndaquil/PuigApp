package org.puig.puigapi.persistence.entity.operation;

import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.persistence.entity.utils.Direccion;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"ubicacion", "hora_abre", "hora_cierra"})
@Document(collection = "finances")
public class Sucursal {
    @Id private String _id;
    private @NotNull String nombre;
    private @NotNull Direccion ubicacion;
    private @NotNull LocalTime hora_abre;
    private @NotNull LocalTime hora_cierra;
}
