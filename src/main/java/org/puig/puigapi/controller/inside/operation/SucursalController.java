package org.puig.puigapi.controller.inside.operation;

import org.puig.puigapi.controller.PersistenceController;
import org.puig.puigapi.controller.responses.ObjectResponse;
import org.puig.puigapi.controller.responses.Response;
import org.puig.puigapi.persistence.entity.operation.Empleado;
import org.puig.puigapi.persistence.entity.operation.Sucursal;
import org.puig.puigapi.util.persistence.SimpleInstance;
import org.puig.puigapi.service.operation.SucursalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/sucursales")
public class SucursalController extends PersistenceController<Sucursal, String, Sucursal.PostRequest> {
    protected SucursalService service;

    @Autowired
    public SucursalController(SucursalService service) {
        super(service);
        this.service = service;
    }

    @PostMapping(value = "empleados/where/sucursal_id/is", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Response> readEmpleadosBySucursalId(
            @RequestBody SimpleInstance<String> uniqueSucursal) {
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
            @RequestBody SimpleInstance<String> uniqueSucursal) {
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
