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
        methods= {
                RequestMethod.GET,
                RequestMethod.POST,
                RequestMethod.DELETE,
                RequestMethod.PUT})
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

        C t = p.instance();
        C saved = service.save(t);

        return ObjectResponse.builder()
                .status(HttpStatus.CREATED)
                .message("Objeto %s fue persistido exitosamente"
                        .formatted(t.getClass().getSimpleName()))
                .body(saved)
                .build()
                .transform();
    }

    @PostMapping(value = "all", produces = "application/json", consumes = "application/json")
    public ResponseEntity<Response> save(@NotEmpty @Valid @RequestBody List<P> ps) {
        logger.post();

        List<C> ts = ps.stream()
                .map(Instantiator::instance)
                .toList();
        List<C> tSaved = service.save(ts);

        return ObjectResponse.builder()
                .status(HttpStatus.OK)
                .message("Objetos %s persistidos correctamente"
                        .formatted(ts.get(0).getClass()))
                .body(tSaved)
                .build()
                .transform();
    }

    @PostMapping(value = "where/id/is", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Response> readByID(
            @NotNull @Valid @RequestBody SimpleInstance<ID> simpleInstance) {
        logger.get();

        C read = service.readById(simpleInstance.id());

        return ObjectResponse.builder()
                .status(HttpStatus.FOUND)
                .message("Busqueda exitosa")
                .body(read)
                .build()
                .transform();
    }

    @PostMapping(value = "where/object/matches", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Response> read(@NotNull @Valid @RequestBody C t) {
        logger.get();

        List<C> entities = service.read(t);

        if (entities.isEmpty()) return ObjectResponse.builder()
                .status(HttpStatus.OK)
                .message("No pudieron ser encontrados coincidencias")
                .body(entities)
                .build()
                .transform();
        return ObjectResponse.builder()
                .status(HttpStatus.FOUND)
                .message("%ss obtenidos correctamente"
                        .formatted(t.getClass().getSimpleName()))
                .body(entities)
                .build()
                .transform();
    }

    //@PreAuthorize("hasAuthority('ADMINISTRADOR_WEB')")
    @GetMapping(produces = "application/json")
    public ResponseEntity<Response> readAll() {
        logger.get();

        List<C> entities = service.readAllWhile();
        System.out.println("entites: " + entities);
        return ObjectResponse.builder()
                .status(HttpStatus.OK)
                .message("Objetos encontrados")
                .body(entities)
                .build()
                .transform();
    }

    @PutMapping(produces = "application/json")
    public ResponseEntity<Response> update(@NotNull @Valid @RequestBody C t) {
        logger.put();

        boolean result = service.update(t);

        if (result)
            return Response.builder()
                    .status(HttpStatus.OK)
                    .message("%s uptaded successfuly"
                            .formatted(t.getClass().getSimpleName()))
                    .build()
                    .transform();

        return Response.builder()
                .status(HttpStatus.NOT_MODIFIED)
                .message("%s was not uptaded successfuly"
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
                    .message("Objeto %s borrado correctamente"
                            .formatted(id))
                    .build()
                    .transform();
        return Response.builder()
                .status(HttpStatus.NOT_MODIFIED)
                .message("No hay objeto que coincida con %s"
                        .formatted(id))
                .build()
                .transform();
    }
}
