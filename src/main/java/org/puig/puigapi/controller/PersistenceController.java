package org.puig.puigapi.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.controller.responses.ObjectResponse;
import org.puig.puigapi.controller.responses.Response;
import org.puig.puigapi.util.PuigLogger;
import org.puig.puigapi.util.persistence.Instantiator;
import org.puig.puigapi.util.persistence.Irrepetibe;
import org.puig.puigapi.util.persistence.SimpleInstance;
import org.puig.puigapi.persistence.repository.PuigRepository;
import org.puig.puigapi.service.PersistenceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador génerico CRUD para la API de PollosPuig.
 * @param <C> La clase relacionada la creación de este servicio.
 * @param <ID> El especializado del ID de la clase.
 * @param <P> La clase Instantiator que se usará como intermediario durante las solicitudes PostRequest.
 */

@CrossOrigin(
        origins = "*",
        methods= {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT})

@Validated
@EnableMethodSecurity
public abstract class PersistenceController
        <C extends Irrepetibe<ID>, ID, P extends Instantiator<C>> {

    protected final PersistenceService <C, ID, ? extends PuigRepository<C, ID>> service;
    protected final PuigLogger logger;

    protected PersistenceController(PersistenceService<C, ID, ? extends PuigRepository<C, ID>> service) {
        this.service = service;
        this.logger = new PuigLogger(getClass());
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity<Response> save(@NotNull @Valid @RequestBody P p) {
        logger.post();

        C c = service.save(p.instance());

        return ObjectResponse.builder()
                .status(HttpStatus.CREATED)
                .message("%s persistido correctamente"
                        .formatted(c.getClass().getSimpleName()))
                .body(c)
                .build()
                .transform();
    }

    @Transactional
    @PostMapping(value = "all", produces = "application/json", consumes = "application/json")
    public ResponseEntity<Response> save(@NotEmpty @Valid @RequestBody List<P> ps) {
        logger.post();

        List<C> collections = service.save(ps.stream()
                .map(Instantiator::instance)
                .toList());
        return ObjectResponse.builder()
                .status(HttpStatus.OK)
                .message("%s %s persistidos correctamente"
                        .formatted(collections.size(), collections.get(0).getClass()))
                .body(collections)
                .build()
                .transform();
    }

    @PostMapping(value = "where/id/is", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Response> readByID(
            @NotNull @Valid @RequestBody SimpleInstance<ID> simpleInstance) {
        logger.get();
        C c = service.readById(simpleInstance);

        return ObjectResponse.builder()
                .status(HttpStatus.OK)
                .message("%s encontrado correctamente"
                        .formatted(c.getClass().getSimpleName()))
                .body(c)
                .build()
                .transform();
    }

    @PostMapping(value = "where/object/matches", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Response> read(@NotNull @Valid @RequestBody C c) {
        logger.get();
        List<C> collections = service.read(c);

        return ObjectResponse.builder()
                .status(HttpStatus.OK)
                .message("Busqueda exitosa de %s"
                        .formatted(c.getClass().getSimpleName()))
                .body(collections)
                .build()
                .transform();
    }

    @PreAuthorize("hasAuthority('ADMINISTRADOR_WEB')")
    @GetMapping(produces = "application/json")
    public ResponseEntity<Response> readAll() {
        logger.get();
        List<C> collections = service.readAllWhile();

        return ObjectResponse.builder()
                .status(HttpStatus.OK)
                .message("Se encontraron %s objetos exitosamente"
                        .formatted(collections.size()))
                .body(collections)
                .build()
                .transform();
    }

    @PutMapping(produces = "application/json")
    public ResponseEntity<Response> update(@NotNull @Valid @RequestBody C t) {
        logger.put();

        if (service.update(t))
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

    @DeleteMapping(produces = "application/json")
    public ResponseEntity<Response> delete(@RequestParam("id") ID id) {
        logger.delete();

        if (service.delete(id))
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
