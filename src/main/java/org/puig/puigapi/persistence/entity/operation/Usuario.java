package org.puig.puigapi.persistence.entity.operation;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.persistence.entity.utils.Direccion;
import org.puig.puigapi.persistence.entity.utils.Tarjeta;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false, exclude = {"direcciones", "tarjetas"})
@Document(collection = "operation")
public class Usuario extends Persona {
    @Id private String _correo;
    private Set<Direccion> direcciones;
    private Set<Tarjeta> tarjetas;
}
