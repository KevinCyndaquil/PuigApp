package org.puig.puigapi.controller;

import org.puig.puigapi.service.PersistenceService;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@CrossOrigin(
        origins = "*",
        methods= {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT})
public abstract class PersistenceController<T, ID> {
    protected final PersistenceService <T, ID> service;
    protected final org.slf4j.Logger logger;

    protected static final String PuigAppHeader = "PuigAPI_reponse";

    protected PersistenceController(PersistenceService<T, ID> service) {
        this.service = service;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

   // @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity<T> save(@RequestBody T t) {
        logger.info("Petición Post a las %s".formatted(LocalDateTime.now()));

        T tSaved = service.save(t);

        if (Objects.isNull(tSaved))
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
                    .header(PuigAppHeader,
                            "%s was not created".formatted(t.getClass().getSimpleName()))
                    .build();

        return ResponseEntity.status(HttpStatus.CREATED)
                .header(PuigAppHeader,
                        "%s was successfuly created".formatted(t.getClass().getSimpleName()))
                .body(tSaved);
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

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<T> readByID(@PathVariable("id") ID id) {
        logger.info("Get petition at:  %s".formatted(LocalDateTime.now()));

        Optional<T> entityRead = service.readByID(id);

        return entityRead
                .map(e -> ResponseEntity.status(HttpStatus.OK)
                        .header(PuigAppHeader, "%s read successfully".formatted(e.getClass().getSimpleName()))
                        .body(e))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.OK)
                        .header(PuigAppHeader, "Entity could not be read")
                        .build());
    }

   //@CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(produces = "application/json")
    public ResponseEntity<List<T>> readAll() {
        logger.info("Get petition at: %s".formatted(LocalDateTime.now()));

        return new ResponseEntity<>(service.readAll(), HttpStatus.OK);
    }

   // @CrossOrigin(origins = "http://localhost:4200")
    @PutMapping(produces = "application/json")
    public Boolean update(@RequestBody T t) {
        logger.info("Put petition at: %s".formatted(LocalDateTime.now()));

        return service.update(t);
    }

    //@CrossOrigin(origins = "http://localhost:4200")
    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<String> delete(@PathVariable("id") ID id) {
        logger.info("Delete petition at: %s".formatted(LocalDateTime.now()));

        boolean result = service.delete(id);

        if (result)
            return new ResponseEntity<>("instancia borrada correctamente", HttpStatus.OK);
        return new ResponseEntity<>(
                "no hay instancia que coincidan con el id %s"
                        .formatted(id), HttpStatus.NOT_MODIFIED);
    }
}
