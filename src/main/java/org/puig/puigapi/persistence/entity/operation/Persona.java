package org.puig.puigapi.persistence.entity.operation;

import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.persistence.entity.DatoBanco;
import org.puig.puigapi.persistence.entity.Direccion;
import org.puig.puigapi.persistence.entity.Social;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false, exclude = {
        "nombre",
        "apellido_paterno",
        "apellido_materno"})
public abstract class Persona extends Social {
    @Id private String _id;
    private @NotNull String nombre;
    private @NotNull String apellido_paterno;
    private String apellido_materno;

    public Persona(String telefono,
                   @NotNull String rfc,
                   DatoBanco datos_bancarios,
                   @NotNull String nombre,
                   @NotNull String apellido_paterno,
                   String apellido_materno) {
        super(telefono, rfc, datos_bancarios);
        this.nombre = nombre;
        this.apellido_paterno = apellido_paterno;
        this.apellido_materno = apellido_materno;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    @Document(collection = "operation")
    public static class Usuario extends Persona {
        private Direccion ubicacion;
        private String correo;
        private String salt;

        public Usuario(String telefono,
                       @NotNull String rfc,
                       DatoBanco datos_bancarios,
                       @NotNull String nombre,
                       @NotNull String apellido_paterno,
                       String apellido_materno,
                       Direccion ubicacion,
                       String correo,
                       String salt) {
            super(telefono, rfc, datos_bancarios, nombre, apellido_paterno, apellido_materno);
            this.ubicacion = ubicacion;
            this.correo = correo;
            this.salt = salt;
        }
    }
}
