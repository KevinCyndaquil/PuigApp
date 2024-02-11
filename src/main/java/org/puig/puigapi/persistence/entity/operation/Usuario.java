package org.puig.puigapi.persistence.entity.operation;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.persistence.entity.utils.Direccion;
import org.puig.puigapi.persistence.entity.utils.Tarjeta;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "operation")
public class Usuario extends Persona {
    private Set<Direccion> direcciones;
    private Set<Tarjeta> tarjetas;
    private String correo;
    private String password;
    private String salt;

    public Usuario(@NotNull String nombre,
                   @NotNull String apellido_paterno,
                   String apellido_materno,
                   @NotNull String rfc,
                   String telefono,
                   Set<Direccion> direcciones,
                   Set<Tarjeta> tarjetas,
                   String correo,
                   String password,
                   String salt) {
        super(null, nombre, apellido_paterno, apellido_materno, rfc, telefono);
        this.direcciones = direcciones;
        this.tarjetas = tarjetas;
        this.correo = correo;
        this.password = password;
        this.salt = salt;
    }
}
