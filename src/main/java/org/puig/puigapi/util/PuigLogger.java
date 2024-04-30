package org.puig.puigapi.util;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class PuigLogger {
    public Logger logger;
    protected static final ZoneId serverZone =
            ZoneId.of("America/Mexico_City");


    public PuigLogger(Class<?> _class) {
        this.logger = LoggerFactory.getLogger(_class);
    }

    public void post() {
        logger.info("Realizando POST a las %s"
                .formatted(LocalDateTime.now().atZone(serverZone)));
    }

    public void post(String method) {
        logger.info("Realizando POST method %s a las %s"
                .formatted(method, LocalDateTime.now().atZone(serverZone)));
    }

    public void get() {
        logger.info("Realizando GET a las %s"
                .formatted(LocalDateTime.now().atZone(serverZone)));
    }

    public void get(String method) {
        logger.info("Realizando GET method %s a las %s"
                .formatted(method, LocalDateTime.now().atZone(serverZone)));
    }

    public void put() {
        logger.info("Realizando PUT a las %s"
                .formatted(LocalDateTime.now().atZone(serverZone)));
    }

    public void delete() {
        logger.info("Realizando DELETE a las %s"
                .formatted(LocalDateTime.now().atZone(serverZone)));
    }

    public void exception(@NotNull Exception e) {
        logger.error(e.getMessage());
    }
}
