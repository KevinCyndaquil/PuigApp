package org.puig.puigapi.controller;

import org.puig.puigapi.controller.responses.ObjectResponse;
import org.puig.puigapi.controller.responses.Response;
import org.puig.puigapi.persistence.entity.utils.Irrepetibe;
import org.puig.puigapi.service.PersistenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@CrossOrigin(
        origins = "*",
        methods= {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT})
public abstract class PersistenceController<T extends Irrepetibe<ID>, ID> {
    protected final PersistenceService <T, ID> service;
    protected final Logger logger;

    protected static final String PuigAppHeader = "PuigAPI_reponse";

    protected PersistenceController(PersistenceService<T, ID> service) {
        this.service = service;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity<T> save(@RequestBody T t) {
        logger.info("Petición Post a las %s".formatted(LocalDateTime.now()));

        T saved = service.save(t);

        if (Objects.isNull(saved))
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
                    .header(PuigAppHeader,
                            "%s was not created".formatted(t.getClass().getSimpleName()))
                    .build();

        return ResponseEntity.status(HttpStatus.CREATED)
                .header(PuigAppHeader,
                        "%s was successfuly created".formatted(t.getClass().getSimpleName()))
                .body(saved);
    }

    @PostMapping(value = "/all", produces = "application/json", consumes = "application/json")
    public ResponseEntity<List<T>> save(@RequestBody List<T> ts) {
        logger.info("Post petition at: %s".formatted(LocalDateTime.now()));

        List<T> tSaved = service.save(ts);

        return ResponseEntity.status(HttpStatus.OK)
                .header(PuigAppHeader, tSaved.stream()
                        .map(t -> t == null ? "was not saved\n" : "%s was saved\n"
                                .formatted(t.getClass().getSimpleName()))
                        .reduce("", String::concat))
                .body(tSaved);
    }

    @GetMapping(value = "by", produces = "application/json")
    public ResponseEntity<T> readByID(@RequestParam("id") ID id) {
        logger.info("Get petition at:  %s".formatted(LocalDateTime.now()));

        T read = service.readByID(id);

        return ResponseEntity.status(HttpStatus.OK)
                .header(PuigAppHeader, "%s read successfully"
                        .formatted(read.getClass().getSimpleName()))
                .body(read);
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
    public Boolean update(@RequestBody T t) {
        logger.info("Put petition at: %s".formatted(LocalDateTime.now()));

        return service.update(t);
    }

    @DeleteMapping(produces = "application/json")
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
    }
}
