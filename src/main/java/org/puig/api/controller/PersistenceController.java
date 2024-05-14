package org.puig.api.controller;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.puig.api.controller.responses.ObjectResponse;
import org.puig.api.controller.responses.Response;
import org.puig.api.service.PersistenceService;
import org.puig.api.util.PuigLogger;
import org.puig.api.util.grupos.InitInfo;
import org.puig.api.util.grupos.SimpleInfo;
import org.puig.api.util.grupos.UniqueInfo;
import org.puig.api.util.persistence.Unico;
import org.puig.api.util.persistence.SimpleInstance;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * Controlador génerico CRUD para la API de PollosPuig.
 * @param <C> La clase relacionada la creación de este servicio.
 */

@CrossOrigin(
        origins = "*",
        methods= {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT})

@Validated
@EnableMethodSecurity
public interface PersistenceController<C extends Unico> {
    PersistenceService<C> service();
    PuigLogger logger();

    @PostMapping(produces = "application/json", consumes = "application/json")
    default ResponseEntity<Response> save(
            @NonNull@Validated({InitInfo.class, SimpleInfo.class})@RequestBody C c) {
        logger().post();

        return ObjectResponse.builder()
                .status(HttpStatus.CREATED)
                .message("%s persistido correctamente"
                        .formatted(c.getClass().getSimpleName()))
                .body(service().save(c))
                .build()
                .transform();
    }

    @PostMapping(value = "all", produces = "application/json", consumes = "application/json")
    default ResponseEntity<Response> save(
            @NotEmpty@Validated({InitInfo.class, SimpleInfo.class})@RequestBody Set<C> cs) {
        logger().post();

        return ObjectResponse.builder()
                .status(HttpStatus.OK)
                .message("%s %s persistidos correctamente"
                        .formatted(cs.size(), service().clazz().getSimpleName()))
                .body(service().save(cs))
                .build()
                .transform();
    }

    @PostMapping(value = "where/id/is", consumes = "application/json", produces = "application/json")
    default ResponseEntity<Response> readByID(
            @NonNull@Validated(UniqueInfo.class)@RequestBody SimpleInstance instance) {
        logger().get();

        return ObjectResponse.builder()
                .status(HttpStatus.OK)
                .message("%s encontrado correctamente"
                        .formatted(service().clazz().getSimpleName()))
                .body(service().readById(instance))
                .build()
                .transform();
    }

    @PostMapping(value = "where/object/matches", consumes = "application/json", produces = "application/json")
    default ResponseEntity<Response> read(
            @NonNull@Validated(UniqueInfo.class)@RequestBody C c) {
        logger().get();

        return ObjectResponse.builder()
                .status(HttpStatus.OK)
                .message("Busqueda exitosa de %s"
                        .formatted(c.getClass().getSimpleName()))
                .body(service().read(c))
                .build()
                .transform();
    }

    @GetMapping(produces = "application/json")
    default ResponseEntity<Response> readAll() {
        logger().get();

        return ObjectResponse.builder()
                .status(HttpStatus.OK)
                .message("Se encontraron objetos exitosamente")
                .body(service().readAllWhile())
                .build()
                .transform();
    }

    @PreAuthorize("hasAuthority('ADMINISTRADOR_WEB') or hasAuthority('GERENTE')")
    @PutMapping(produces = "application/json")
    default ResponseEntity<Response> update(
            @NotNull@Validated({InitInfo.class, UniqueInfo.class})@RequestBody C t) {
        logger().put();

        if (service().update(t))
            return Response.builder()
                    .status(HttpStatus.OK)
                    .message("%s actualizado correctamnete"
                            .formatted(t.getClass().getSimpleName()))
                    .build()
                    .transform();
        return Response.builder()
                .status(HttpStatus.NOT_MODIFIED)
                .message("%s no modificado"
                        .formatted(t.getClass().getSimpleName()))
                .build()
                .transform();
    }

    @PreAuthorize("hasAuthority('ADMINISTRADOR_WEB')")
    @DeleteMapping(produces = "application/json")
    default ResponseEntity<Response> delete(@RequestParam("id") ObjectId id) {
        logger().delete();

        if (service().delete(id))
            return Response.builder()
                    .status(HttpStatus.OK)
                    .message("%s borrado correctamente"
                            .formatted(id))
                    .build()
                    .transform();
        return Response.builder()
                .status(HttpStatus.NOT_MODIFIED)
                .message("No hay objeto que coincida con el ID %s"
                        .formatted(id))
                .build()
                .transform();
    }
}
