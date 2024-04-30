package org.puig.puigapi.controller.outside;

import jakarta.validation.Valid;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.controller.AuthController;
import org.puig.puigapi.controller.PersistenceController;
import org.puig.puigapi.controller.responses.ErrorResponse;
import org.puig.puigapi.controller.responses.ObjectResponse;
import org.puig.puigapi.controller.responses.Response;
import org.puig.puigapi.persistence.entity.operation.Empleado;
import org.puig.puigapi.util.persistence.Credentials;
import org.puig.puigapi.service.operation.EmpleadoService;
import org.puig.puigapi.service.operation.SucursalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Setter(onMethod_ = @Autowired)
@RequestMapping("/empleados")
public class EmpleadoController extends AuthController<Empleado, Empleado.PostRequest> {

    protected final EmpleadoService service;
    protected SucursalService sucursalService;

    @Autowired
    public EmpleadoController(EmpleadoService service) {
        super(service);
        this.service = service;
    }

    @Valid
    @Override
    public ResponseEntity<Response> save(@NotNull Empleado.PostRequest request) {
        Empleado saved = sucursalService.generarAlta(
                request.getSucursal(),
                request.instance());

        return ObjectResponse.builder()
                .status(HttpStatus.CREATED)
                .message("%s was succesfuly created"
                        .formatted(saved.getClass().getSimpleName()))
                .body(saved)
                .build()
                .transform();
    }

    @PostMapping("update/estado")
    public ResponseEntity<Response> updateEstado(@NotNull @RequestBody @Valid Empleado.UpdateEstadoRequest updater) {
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
}