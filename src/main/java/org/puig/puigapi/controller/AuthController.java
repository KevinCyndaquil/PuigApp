package org.puig.puigapi.controller;

import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.controller.responses.ErrorResponse;
import org.puig.puigapi.controller.responses.ObjectResponse;
import org.puig.puigapi.controller.responses.Response;
import org.puig.puigapi.persistence.repository.PuigRepository;
import org.puig.puigapi.service.auth.AuthService;
import org.puig.puigapi.util.Persona;
import org.puig.puigapi.util.persistence.Credentials;
import org.puig.puigapi.util.persistence.Instantiator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

/**
 * Métodos génericos para la creación de un servicio de Autenticación.
 * @param <U> La clase o derivada de persona a ser guardada en la base de datos.
 * @param <P> El especializado del ID de la persona.
 */
public class AuthController <U extends Persona, P extends Instantiator<U>>
        extends PersistenceController<U, ObjectId, P> {
    protected AuthService<U, ? extends PuigRepository<U, ObjectId>> service;

    protected AuthController(AuthService<U, ? extends PuigRepository<U, ObjectId>> service) {
        super(service);
        this.service = service;
    }

    @PostMapping("auth/register")
    public ResponseEntity<Response> register(@NotNull @Valid @RequestBody P p) {
        logger.post("register");

        U u = p.instance();
        String token = service.register(u);

        if (token == null)
            return ErrorResponse.builder()
                    .status(HttpStatus.NOT_MODIFIED)
                    .message("Usuario no pudo ser registrado correctamente")
                    .hint("Intenta revisar la estructura del usuario")
                    .build()
                    .transform();
        return ObjectResponse.builder()
                .status(HttpStatus.CREATED)
                .message("Usuario %s registrado correcmente"
                        .formatted(u.getNombre()))
                .body(token)
                .build()
                .transform();
    }

    @PostMapping("auth/login")
    public ResponseEntity<Response> login(@NotNull @Valid @RequestBody Credentials credential) {
        logger.get("login");

        Optional<String> result = service.login(credential);

        return result.map(token -> ObjectResponse.builder()
                        .status(HttpStatus.FOUND)
                        .message("Usuario %s logeado correctamente"
                                .formatted(credential.identifier()))
                        .body(token)
                        .build()
                        .transform())
                .orElseGet(() -> ErrorResponse.builder()
                        .status(HttpStatus.NOT_FOUND)
                        .message("Usuario %s no pudo ser logeado"
                                .formatted(credential.identifier()))
                        .hint("Intenta registrarte primero")
                        .build()
                        .transform());
    }
}
