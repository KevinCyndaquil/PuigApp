package org.puig.puigapi.persistence.entity.operation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.persistence.entity.utils.Direccion;
import org.puig.puigapi.persistence.entity.utils.Irrepetibe;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"ubicacion", "hora_abre", "hora_cierra"})
@Document(collection = "finances")
public class Sucursal implements Irrepetibe<String> {
    @Id @JsonProperty(access = Access.READ_ONLY) private String id;
    @NotNull private String nombre;
    @NotNull private Direccion ubicacion;
    @NotNull private LocalTime hora_abre;
    @NotNull private LocalTime hora_cierra;
}
