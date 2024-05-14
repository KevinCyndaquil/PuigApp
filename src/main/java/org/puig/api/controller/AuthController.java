package org.puig.api.controller;

import lombok.NonNull;
import org.puig.api.controller.responses.ErrorResponse;
import org.puig.api.controller.responses.ObjectResponse;
import org.puig.api.controller.responses.Response;
import org.puig.api.service.auth.AuthService;
import org.puig.api.util.PuigLogger;
import org.puig.api.util.PuigUser;
import org.puig.api.util.data.Tokenisable;
import org.puig.api.util.grupos.InitInfo;
import org.puig.api.util.grupos.SimpleInfo;
import org.puig.api.util.persistence.Credentials;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Métodos genéricos para la creación de un servicio de Autenticación.
 * @param <U> La clase o derivada de persona a ser guardada en la base de datos.
 */
public interface AuthController <U extends PuigUser> {
    @NonNull AuthService<U> service();
    @NonNull PuigLogger logger();

    @PostMapping(value = "auth/register", consumes = "application/json", produces = "application/json")
    default ResponseEntity<Response> register(
            @NonNull@Validated({InitInfo.class, SimpleInfo.class})@RequestBody U u) {
        logger().post("register");

        Tokenisable<U> token = service().register(u);

        if (token == null)
            return ErrorResponse.builder()
                    .status(HttpStatus.NOT_MODIFIED)
                    .message("Usuario no pudo ser registrado correctamente")
                    .hint("Intenta revisar la estructura del usuario")
                    .build()
                    .transform();
        return ObjectResponse.builder()
                .status(HttpStatus.CREATED)
                .message("Usuario %s registrado correctamente"
                        .formatted(token.getUser().getNombre()))
                .body(token)
                .build()
                .transform();
    }

    @PostMapping(value = "auth/login", consumes = "application/json", produces = "application/json")
    default ResponseEntity<Response> login(
            @NonNull@Validated(SimpleInfo.class)@RequestBody Credentials credential) {
        logger().get("login");
        Tokenisable<U> token = service().login(credential);

        return ObjectResponse.builder()
                .status(HttpStatus.OK)
                .message("Usuario %s inició sesión correctamente"
                        .formatted(credential.identifier()))
                .body(token)
                .build()
                .transform();
    }
}
