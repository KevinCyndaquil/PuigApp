package org.puig.puigapi.service.annotations;

import org.puig.puigapi.persistence.entity.utils.persistence.Irrepetibe;
import org.springframework.stereotype.Service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Service
public @interface PuigService {
    Class<? extends Irrepetibe<?>> value();
}
