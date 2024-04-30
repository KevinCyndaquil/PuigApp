package org.puig.puigapi.util.annotation;

import org.puig.puigapi.util.persistence.Irrepetibe;
import org.springframework.stereotype.Service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Se debe utilizar en los servicios de Pollos Puig. Incluye una referencia a la clase que tiene
 * su anotación @Collection
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)

@Service
public @interface PuigService {
    Class<? extends Irrepetibe<?>> value();
}
