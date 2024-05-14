package org.puig.api.controller.outside;

import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.puig.api.controller.AuthController;
import org.puig.api.controller.PersistenceController;
import org.puig.api.controller.responses.ObjectResponse;
import org.puig.api.controller.responses.Response;
import org.puig.api.util.PuigLogger;
import org.puig.api.util.data.Correo;
import org.puig.api.util.data.Telefono;
import org.puig.api.persistence.entity.operation.Usuario;
import org.puig.api.service.operation.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "usuarios")
@RequiredArgsConstructor
public class UsuarioController implements AuthController<Usuario>, PersistenceController<Usuario> {
    private final UsuarioService service;
    private final PuigLogger logger = new PuigLogger(UsuarioController.class);

    @Override
    public @NonNull UsuarioService service() {
        return service;
    }

    @Override
    public @NonNull PuigLogger logger() {
        return logger;
    }

    @PostMapping("where/correo_telefono/is")
    public ResponseEntity<Response> readByCorreoOrTelefono(@NonNull@Valid@RequestBody Usuario.ID id) {
        Usuario usuario;
        Object obj;

        if (id.getTelefono() != null) {
            obj = id.getTelefono();
            usuario = service.readByTelefono(id.getTelefono());
        } else if (id.getCorreo() != null) {
            obj = id.getCorreo();
            usuario = service.readByCorreo(id.getCorreo());
        } else throw new IllegalArgumentException(
                "Se debe proporcionar un Correo o Telefono para leer un Usuario");

        return ObjectResponse.builder()
                .status(HttpStatus.OK)
                .message("Usuario %s encontrado con ID %s"
                        .formatted(usuario.getNombre(), obj))
                .body(usuario)
                .build()
                .transform();
    }

    @PostMapping("where/correo/is")
    public ResponseEntity<Response> readByCorreo(@NonNull @Valid @RequestBody Correo correo) {
        Usuario usuario = service.readByCorreo(correo);

        return ObjectResponse.builder()
                .status(HttpStatus.OK)
                .message("Usuario encontrado")
                .body(usuario)
                .build()
                .transform();
    }

    @PostMapping("where/telefono/is")
    public ResponseEntity<Response> readByTelefono(@NonNull @Valid @RequestBody Telefono telefono) {
        Usuario usuario = service.readByTelefono(telefono);

        return ObjectResponse.builder()
                .status(HttpStatus.OK)
                .message("Usuario encontrado")
                .body(usuario)
                .build()
                .transform();
    }
}
