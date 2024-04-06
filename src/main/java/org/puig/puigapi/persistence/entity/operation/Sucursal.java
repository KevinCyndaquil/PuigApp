package org.puig.puigapi.persistence.entity.operation;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.persistence.entity.utils.Direccion;
import org.puig.puigapi.persistence.entity.utils.Irrepetibe;
import org.puig.puigapi.persistence.entity.utils.PostEntity;
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
    @Id private String id;
    @NotNull private String nombre;
    @NotNull private Direccion ubicacion;
    @NotNull @JsonFormat(pattern = "HH:mm:ss") private LocalTime hora_abre;
    @NotNull @JsonFormat(pattern = "HH:mm:ss") private LocalTime hora_cierra;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Post implements PostEntity<Sucursal> {
        @NotNull private String nombre;
        @NotNull private Direccion ubicacion;
        @NotNull private LocalTime hora_abre;
        @NotNull private LocalTime hora_cierra;

        @Override
        public Sucursal instance() {
            return Sucursal.builder()
                    .nombre(nombre)
                    .ubicacion(ubicacion)
                    .hora_abre(hora_abre)
                    .hora_cierra(hora_cierra)
                    .build();
        }
    }
}
