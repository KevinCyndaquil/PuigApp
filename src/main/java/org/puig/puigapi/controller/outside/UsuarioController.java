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

    @Valid
    @PostMapping("where/correo/is")
    public ResponseEntity<Response> readByCorreo(@NotNull Correo correo) {
        Usuario usuario = service.readByCorreo(correo);

        return ObjectResponse.builder()
                .status(HttpStatus.OK)
                .message("Usuario encontrado")
                .body(usuario)
                .build()
                .transform();
    }

    @Valid
    @PostMapping("where/telefono/is")
    public ResponseEntity<Response> readByTelefono(@NotNull Telefono telefono) {
        Usuario usuario = service.readByTelefono(telefono);

        return ObjectResponse.builder()
                .status(HttpStatus.OK)
                .message("Usuario encontrado")
                .body(usuario)
                .build()
                .transform();
    }
}
