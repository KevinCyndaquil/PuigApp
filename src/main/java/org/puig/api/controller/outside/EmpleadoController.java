package org.puig.api.controller.outside;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.puig.api.controller.AuthController;
import org.puig.api.controller.PersistenceController;
import org.puig.api.controller.responses.ObjectResponse;
import org.puig.api.controller.responses.Response;
import org.puig.api.util.PuigLogger;
import org.puig.api.util.errors.BusquedaSinResultadoException;
import org.puig.api.persistence.entity.operation.Empleado;
import org.puig.api.service.operation.EmpleadoService;
import org.puig.api.service.operation.SucursalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/empleados")
@RequiredArgsConstructor
public class EmpleadoController implements PersistenceController<Empleado>, AuthController<Empleado> {
    final EmpleadoService service;
    final PuigLogger logger = new PuigLogger(EmpleadoController.class);

    final SucursalService sucursalService;

    @Override
    public @NonNull EmpleadoService service() {
        return service;
    }

    @Override
    public @NonNull PuigLogger logger() {
        return logger;
    }

    /*
    @Override
    public ResponseEntity<Response> register(@Valid @NonNull Empleado postEmpleado) {
        Empleado empleado = postEmpleado;
        Tokenisable<Empleado> token = service.alta(empleado, postEmpleado.getSucursal());

        if (token == null)
            return ErrorResponse.builder()
                    .status(HttpStatus.NOT_MODIFIED)
                    .message("Usuario no pudo ser registrado correctamente")
                    .hint("Intenta revisar la estructura del usuario")
                    .build()
                    .transform();
        return ObjectResponse.builder()
                .status(HttpStatus.CREATED)
                .message("Usuario %s registrado correctamente"
                        .formatted(empleado.getNombre()))
                .body(token)
                .build()
                .transform();
    }*/

    @PostMapping("update/estado")
    public ResponseEntity<Response> updateEstado(@NotNull @RequestBody @Valid Empleado.UpdateEstadoRequest updater) {
        Empleado read = service.actualizarEmpleado(
                updater.getSucursal(),
                updater.getEmpleado(),
                updater.getEstado());

        return ObjectResponse.builder()
                .status(HttpStatus.OK)
                .message("%s was successfully generated with %s status"
                        .formatted(read.getClass().getSimpleName(), updater.getEstado()))
                .body(read)
                .build()
                .transform();
    }

    @PostMapping("where/nickname/is")
    public ResponseEntity<Response> readByNickname(@RequestBody Empleado empleadoNickname) {
        System.out.println(empleadoNickname);
        Empleado read = service.readByNickname(empleadoNickname.getNickname())
                .orElseThrow(() -> new BusquedaSinResultadoException("nickname", empleadoNickname.getNickname()));

        return ObjectResponse.builder()
                .status(HttpStatus.OK)
                .message("%s encontrado"
                        .formatted(read.getNombre()))
                .body(read)
                .build()
                .transform();
    }
}