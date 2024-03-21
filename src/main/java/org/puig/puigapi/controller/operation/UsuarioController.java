package org.puig.puigapi.controller.operation;

import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.controller.PersistenceController;
import org.puig.puigapi.persistence.entity.operation.Usuario;
import org.puig.puigapi.service.operation.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UsuarioController extends PersistenceController<Usuario, String> {
    private final UsuarioService service;

    @Autowired
    public UsuarioController(UsuarioService service) {
        super(service );
        this.service = service;
    }

    @GetMapping("login")
    private @NotNull ResponseEntity<Usuario> logIn(@RequestParam("correo") String correo,
                                                   @RequestParam("passwd") String password) {
        var usuario = service.findByCredential(correo, password);

        return usuario
                .map(u -> ResponseEntity
                        .status(HttpStatus.OK)
                        .header(PuigAppHeader,
                                "User %s was logged successfully!".formatted(correo))
                        .body(u))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.OK)
                        .header(PuigAppHeader,
                                "%s was not found using %s and password %s"
                                        .formatted(
                                                Usuario.class.getSimpleName(),
                                                correo,
                                                password))
                        .build());
    }
}
