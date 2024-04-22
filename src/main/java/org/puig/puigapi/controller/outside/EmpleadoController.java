package org.puig.puigapi.controller.outside;

import jakarta.validation.Valid;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.controller.AuthController;
import org.puig.puigapi.controller.PersistenceController;
import org.puig.puigapi.controller.responses.ObjectResponse;
import org.puig.puigapi.controller.responses.Response;
import org.puig.puigapi.persistence.entity.operation.Empleado;
import org.puig.puigapi.persistence.entity.utils.data.Correo;
import org.puig.puigapi.persistence.entity.utils.persistence.Credentials;
import org.puig.puigapi.service.operation.EmpleadoService;
import org.puig.puigapi.service.operation.SucursalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@Setter(onMethod_ = @Autowired)
@RequestMapping("/empleados")
public class EmpleadoController
        extends PersistenceController<Empleado, String, Empleado.Request>
        implements AuthController<Empleado.Request, String> {

    protected final EmpleadoService service;
    protected SucursalService sucursalService;

    @Autowired
    public EmpleadoController(EmpleadoService service) {
        super(service);
        this.service = service;
    }

    @Valid
    @Override
    public ResponseEntity<Response> save(@NotNull Empleado.Request request) {
        Empleado saved = sucursalService.generarAlta(request.getSucursal_alta(), request.instance());

        return ObjectResponse.builder()
                .status(HttpStatus.CREATED)
                .message("%s was succesfuly created"
                        .formatted(saved.getClass().getSimpleName()))
                .body(saved)
                .build()
                .transform();
    }

    @PostMapping("update/estado")
    public ResponseEntity<Response> updateEstado(@NotNull @RequestBody @Valid Empleado.Updater updater) {
        Empleado read = sucursalService.actualizarEmpleado(
                updater.getSucursal(),
                updater.getEmpleado(),
                updater.getEstado());

        return ObjectResponse.builder()
                .status(HttpStatus.OK)
                .message("%s was succesfuly generated with %s status"
                        .formatted(read.getClass().getSimpleName(), updater.getEstado()))
                .body(read)
                .build()
                .transform();
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