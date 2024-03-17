package org.puig.puigapi.controller;

import com.mongodb.internal.diagnostics.logging.Logger;
import org.puig.puigapi.service.PersistenceService;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public abstract class PersistenceController<T, ID> {
    protected final PersistenceService <T, ID> service;
    protected final org.slf4j.Logger logger;

    protected PersistenceController(PersistenceService<T, ID> service) {
        this.service = service;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

   // @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity<T> save(@RequestBody T t) {
        logger.info("Petición Post a las %s".formatted(LocalDateTime.now()));

        T entitySaved = service.save(t);

        if (Objects.isNull(entitySaved))
            return new ResponseEntity<>(t, HttpStatus.NOT_MODIFIED);
        return new ResponseEntity<>(entitySaved, HttpStatus.OK);
    }

    @PostMapping(value = "/all", produces = "application/json")
    public ResponseEntity<List<T>> save(@RequestBody List<T> ts) {
        logger.info("Post petition at: %s".formatted(LocalDateTime.now()));

        List<T> entitiesSaved = service.save(ts);

        if (Objects.isNull(entitiesSaved))
            return new ResponseEntity<>(ts, HttpStatus.NOT_MODIFIED);
        return new ResponseEntity<>(entitiesSaved, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<T> readByID(@PathVariable("id") ID id) {
        logger.info("Get petition at:  %s".formatted(LocalDateTime.now()));

        Optional<T> entityRead = service.readByID(id);

        return entityRead
                .map(e -> new ResponseEntity<>(e, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
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
