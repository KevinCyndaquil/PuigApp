package org.puig.puigapi.controller;

import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.controller.responses.ObjectResponse;
import org.puig.puigapi.controller.responses.Response;
import org.puig.puigapi.persistence.entity.utils.Irrepetibe;
import org.puig.puigapi.persistence.entity.utils.PostEntity;
import org.puig.puigapi.persistence.entity.utils.SimpleInstance;
import org.puig.puigapi.service.PersistenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controlador génerico CRUD para la API de PollosPuig.
 * @param <T> La clase relacionada la creación de este servicio.
 * @param <ID> El tipo del ID de la clase.
 * @param <P> La clase PostEntity que se usará como intermediario durante las solicitudes Post.
 */

@CrossOrigin(
        origins = "*",
        methods= {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT})
public abstract class PersistenceController
        <T extends Irrepetibe<ID>, ID, P extends PostEntity<T>> {
    protected final PersistenceService <T, ID> service;
    protected final Logger logger;

    protected static final String PuigAppHeader = "PuigAPI_reponse";

    protected PersistenceController(PersistenceService<T, ID> service) {
        this.service = service;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity<Response> save(@NotNull @RequestBody P p) {
        logger.info("Petición Post a las %s".formatted(LocalDateTime.now()));

        T t = p.instance();
        T saved = service.save(t);

        if (saved == null)
            return Response.builder()
                    .status(HttpStatus.NOT_MODIFIED)
                    .message("%s was not created"
                            .formatted(t.getClass().getSimpleName()))
                    .build()
                    .transform();
        return ObjectResponse.builder()
                .status(HttpStatus.CREATED)
                .message("%s was succesfuly created"
                        .formatted(t.getClass().getSimpleName()))
                .body(saved)
                .build()
                .transform();
    }

    @PostMapping(value = "/all", produces = "application/json", consumes = "application/json")
    public ResponseEntity<Response> save(@NotNull @RequestBody List<P> ps) {
        logger.info("Post petition at: %s".formatted(LocalDateTime.now()));

        List<T> ts = ps.stream()
                .map(PostEntity::instance)
                .toList();
        List<T> tSaved = service.save(ts);

        return ObjectResponse.builder()
                .status(HttpStatus.OK)
                .message("Saving result was")
                .body(tSaved)
                .build()
                .transform();
    }

    @PostMapping(value = "only_id", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Response> readByID(@NotNull @RequestBody SimpleInstance<ID> simpleInstance) {
        logger.info("Get petition at:  %s".formatted(LocalDateTime.now()));

        T read = service.readByID(simpleInstance.id());

        return ObjectResponse.builder()
                .status(HttpStatus.OK)
                .message("Read successfully")
                .body(read)
                .build()
                .transform();
    }

    @PostMapping(value = "read", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Response> read(@RequestBody T t) {
        logger.info("Get petition at:  %s".formatted(LocalDateTime.now()));

        List<T> entities = service.read(t);

        if (entities.isEmpty()) return ObjectResponse.builder()
                .status(HttpStatus.OK)
                .message("No pudieron ser encontrados coincidencias")
                .body(entities)
                .build()
                .transform();
        return ObjectResponse.builder()
                .status(HttpStatus.OK)
                .message("%s obtenidos correctamente"
                        .formatted(t.getClass().getSimpleName()))
                .body(entities)
                .build()
                .transform();
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<Response> readAll() {
        logger.info("Get petition at: %s".formatted(LocalDateTime.now()));

        List<T> entities = service.readAll();
        System.out.println("entites: " + entities);
        return ObjectResponse.builder()
                .status(HttpStatus.OK)
                .message("Objetos encontrados")
                .body(entities)
                .build()
                .transform();
    }

    @PutMapping(produces = "application/json")
    public ResponseEntity<Response> update(@RequestBody T t) {
        logger.info("Put petition at: %s".formatted(LocalDateTime.now()));

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

    @DeleteMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<Response> delete(@NotNull @RequestBody SimpleInstance<ID> simpleInstance) {
        logger.info("Delete petition at: %s".formatted(LocalDateTime.now()));

        boolean result = service.delete(simpleInstance.id());

        if (result)
            return Response.builder()
                    .status(HttpStatus.OK)
                    .message("Objeto %s borrado correctamente"
                            .formatted(simpleInstance))
                    .build()
                    .transform();
        return Response.builder()
                .status(HttpStatus.NOT_MODIFIED)
                .message("No hay objeto que coincida con %s"
                        .formatted(simpleInstance))
                .build()
                .transform();
    }

    /*@DeleteMapping(produces = "application/json")
    public ResponseEntity<Response> delete(@RequestParam("id") ID id) {
        logger.info("Delete petition at: %s".formatted(LocalDateTime.now()));

        boolean result = service.delete(id);

        if (result)
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
    }*/
}
