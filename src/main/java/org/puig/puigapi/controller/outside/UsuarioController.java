package org.puig.puigapi.controller.outside;

import jakarta.validation.Valid;
import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.controller.AuthController;
import org.puig.puigapi.controller.responses.ObjectResponse;
import org.puig.puigapi.controller.responses.Response;
import org.puig.puigapi.util.data.Correo;
import org.puig.puigapi.util.data.Telefono;
import org.puig.puigapi.persistence.entity.operation.Usuario;
import org.puig.puigapi.service.operation.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "usuarios")
public class UsuarioController extends AuthController<Usuario, Usuario.PostRequest> {
    private final UsuarioService service;

    @Autowired
    public UsuarioController(UsuarioService service) {
        super(service);
        this.service = service;
    }

    @PostMapping("where/correo_telefono/is")
    public ResponseEntity<Response> readByCorreoOrTelefono(@NotNull @Valid @RequestBody Usuario.ID id) {
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
    public ResponseEntity<Response> readByCorreo(@NotNull @Valid @RequestBody Correo correo) {
        Usuario usuario = service.readByCorreo(correo);

        return ObjectResponse.builder()
                .status(HttpStatus.OK)
                .message("Usuario encontrado")
                .body(usuario)
                .build()
                .transform();
    }

    @PostMapping("where/telefono/is")
    public ResponseEntity<Response> readByTelefono(@NotNull @Valid @RequestBody Telefono telefono) {
        Usuario usuario = service.readByTelefono(telefono);

        return ObjectResponse.builder()
                .status(HttpStatus.OK)
                .message("Usuario encontrado")
                .body(usuario)
                .build()
                .transform();
    }
}
