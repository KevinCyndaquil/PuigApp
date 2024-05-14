package org.puig.api.controller.inside.operation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.puig.api.controller.PersistenceController;
import org.puig.api.controller.responses.ObjectResponse;
import org.puig.api.controller.responses.Response;
import org.puig.api.persistence.entity.operation.Empleado;
import org.puig.api.persistence.entity.operation.Sucursal;
import org.puig.api.util.PuigLogger;
import org.puig.api.util.grupos.SimpleInfo;
import org.puig.api.util.persistence.SimpleInstance;
import org.puig.api.service.operation.SucursalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/sucursales")
@RequiredArgsConstructor
public class SucursalController implements PersistenceController<Sucursal> {
    final SucursalService service;
    final PuigLogger logger = new PuigLogger(Sucursal.class);

    @Override
    public SucursalService service() {
        return service;
    }

    @Override
    public PuigLogger logger() {
        return logger;
    }

    @PostMapping(value = "empleados/where/sucursal_id/is", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Response> readEmpleadosBySucursalId(
            @NonNull@RequestBody@Validated(SimpleInfo.class) SimpleInstance uniqueSucursal) {
        Set<Empleado.Detalle> empleados = service.obtenerEmpleados(uniqueSucursal.id());

        return ObjectResponse.builder()
                .status(HttpStatus.OK)
                .message("Empleados de sucursal %s encontrados"
                        .formatted(uniqueSucursal.id()))
                .body(empleados)
                .build()
                .transform();
    }

    @PostMapping(value = "bodega/where/sucursal_id/is", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Response> readBodegaBySucursalId(
            @NonNull@RequestBody@Validated(SimpleInfo.class) SimpleInstance uniqueSucursal) {
        Sucursal.Bodega empleados = service.obtenerBodega(uniqueSucursal.id());

        return ObjectResponse.builder()
                .status(HttpStatus.OK)
                .message("Bodega de sucursal %s encontrada"
                        .formatted(uniqueSucursal.id()))
                .body(empleados)
                .build()
                .transform();
    }
}
