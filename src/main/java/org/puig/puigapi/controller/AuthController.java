package org.puig.puigapi.controller;

import jakarta.validation.Valid;
import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.persistence.entity.utils.persistence.Credentials;
import org.puig.puigapi.persistence.entity.utils.persistence.PostEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Métodos génericos para la creación de un servicio de Autenticación.
 * @param <P> La clase o derivada de persona a ser guardada en la base de datos.
 * @param <ID> El especializado del ID de la persona.
 */
public interface AuthController <P extends PostEntity<?>, ID> {


    @PostMapping("auth/register")
    @NotNull
    ResponseEntity<String> register(@NotNull @Valid @RequestBody P p);

    @PostMapping("auth/login")
    @NotNull
    ResponseEntity<String> login(@NotNull @Valid @RequestBody Credentials<ID> credential);
}
