package org.puig.puigapi.controller.outside;

import jakarta.validation.Valid;
import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.controller.AuthController;
import org.puig.puigapi.controller.responses.ObjectResponse;
import org.puig.puigapi.controller.responses.Response;
import org.puig.puigapi.persistence.entity.utils.data.Correo;
import org.puig.puigapi.persistence.entity.utils.data.Telefono;
import org.puig.puigapi.persistence.entity.utils.persistence.Credentials;
import org.puig.puigapi.controller.PersistenceController;
import org.puig.puigapi.persistence.entity.operation.Usuario;
import org.puig.puigapi.service.operation.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping(value = "usuarios")
public class UsuarioController extends PersistenceController<Usuario, String, Usuario.Request>
        implements AuthController<Usuario.Request, String> {
    private final UsuarioService service;

    @Autowired
    public UsuarioController(UsuarioService service) {
        super(service );
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

    @Override
    public @NotNull ResponseEntity<String> register(@RequestBody @NotNull Usuario.Request usuarioPost) {
        logger.info("Petición register a las %s".formatted(LocalDateTime.now()));

        String token = service.register(usuarioPost.instance());

        if (token == null) return ResponseEntity.status(403)
                .header(PuigAppHeader, "User was not register")
                .body("User was not register");

        return ResponseEntity.status(HttpStatus.OK)
                .header(PuigAppHeader, "User registered")
                .body(token);
    }

    public @NotNull ResponseEntity<String> login(@RequestBody @NotNull Credentials<String> credential) {
        logger.info("Petición login a las %s".formatted(LocalDateTime.now()));

        Optional<String> token = service.login(credential);

        return token.map(string -> ResponseEntity.status(HttpStatus.OK)
                        .header(PuigAppHeader, "User logged")
                        .body(string))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header(PuigAppHeader, "User was not found")
                        .build());
    }
}
