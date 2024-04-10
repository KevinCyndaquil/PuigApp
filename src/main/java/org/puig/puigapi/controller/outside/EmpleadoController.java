package org.puig.puigapi.controller.outside;

import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.controller.AuthController;
import org.puig.puigapi.controller.PersistenceController;
import org.puig.puigapi.persistence.entity.operation.Empleado;
import org.puig.puigapi.persistence.entity.utils.persistence.Credentials;
import org.puig.puigapi.service.operation.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/empleados")
public class EmpleadoController
        extends PersistenceController<Empleado, String, Empleado.Request>
        implements AuthController<Empleado.Request, String> {

    protected final EmpleadoService service;

    @Autowired
    public EmpleadoController(EmpleadoService service) {
        super(service);
        this.service = service;
    }

    @Override
    public @NotNull ResponseEntity<String> register(@NotNull Empleado.Request empleadoPost) {
        logger.info("Petición register a las %s".formatted(LocalDateTime.now()));

        String token = service.register(empleadoPost.instance());

        if (token == null) return ResponseEntity.status(403)
                .header(PuigAppHeader, "Employee was not register")
                .body("Employee was not register");

        return ResponseEntity.status(HttpStatus.OK)
                .header(PuigAppHeader, "Employee registered")
                .body(token);
    }

    @Override
    public @NotNull ResponseEntity<String> login(@NotNull Credentials<String> credential) {
        logger.info("Petición login a las %s".formatted(LocalDateTime.now()));

        Optional<String> token = service.login(credential);

        return token.map(string -> ResponseEntity.status(HttpStatus.OK)
                        .header(PuigAppHeader, "Employee logged")
                        .body(string))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header(PuigAppHeader, "Employee was not found")
                        .build());
    }
}