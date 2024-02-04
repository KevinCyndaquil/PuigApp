package org.puig.puigapi.persistence.entity;

import lombok.*;
import org.springframework.data.annotation.Id;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@EqualsAndHashCode(exclude = {"precio"})
public class IMenu {
    @Id private String _codigo;
    private String nombre;
    private float precio;
}
