package org.puig.puigapi.persistence.entity.finances;

import lombok.*;
import org.springframework.data.annotation.Id;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@EqualsAndHashCode(exclude = {"precio"})
public abstract class Articulo {
    @Id private String _codigo;
    private String nombre;
    private float precio;
}
