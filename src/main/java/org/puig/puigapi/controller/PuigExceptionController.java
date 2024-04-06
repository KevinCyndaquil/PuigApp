package org.puig.puigapi.controller;

import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.controller.responses.Response;
import org.puig.puigapi.controller.responses.ErrorResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.security.SignatureException;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class PuigExceptionController {

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<Response> handleTokenException$Invalid(@NotNull SignatureException e) {
        return ErrorResponse.builder()
                .error("signature_error")
                .status(HttpStatus.UNAUTHORIZED)
                .message(e.getMessage())
                .hint("Intenta obtener primero un token valido")
                .build()
                .transform();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Response> handleIllegalArgument(@NotNull IllegalArgumentException e) {
        return ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .error("argumento_invalido_error")
                .message(e.getMessage())
                .hint("Intenta leer la documentación de la api")
                .build()
                .transform();
    }
}
