package org.puig.puigapi.controller;

import com.mongodb.MongoWriteException;
import org.jetbrains.annotations.NotNull;
import org.puig.puigapi.controller.responses.Response;
import org.puig.puigapi.controller.responses.ErrorResponse;
import org.puig.puigapi.errors.BusquedaSinResultadoException;
import org.puig.puigapi.errors.CreacionVentaException;
import org.puig.puigapi.errors.LlaveDuplicadaException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.security.SignatureException;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ExceptionController {

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response> handleBind(@NotNull MethodArgumentNotValidException e) {
        String message = e.getFieldError() == null ?
                "not defined" :
                e.getFieldError().getDefaultMessage();

        return ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .error("error_de_atributo")
                .message(message)
                .hint("Utiliza el formato correcto para el campo (%s)"
                        .formatted(e.getFieldError().getCode()))
                .build()
                .transform();
    }

    @ExceptionHandler(CreacionVentaException.class)
    public ResponseEntity<Response> handleCreacionVenta(@NotNull CreacionVentaException e) {
        return ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .error("error_realizacion_venta")
                .message(e.getMessage())
                .hint(e.getHint())
                .build()
                .transform();
    }

    @ExceptionHandler(MongoWriteException.class)
    public ResponseEntity<Response> handleMongoWrite() {
        return ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .error("mongo_write_error")
                .message("Ocurrio un error interno del servidor")
                .hint("Llama a Dios")
                .build()
                .transform();
    }

    @ExceptionHandler(LlaveDuplicadaException.class)
    public ResponseEntity<Response> handleLlaveDuplicada(@NotNull LlaveDuplicadaException e) {
        return ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .error("llave_duplicada")
                .message(e.getMessage())
                .hint("Escoge otro identificador en lugar de %s para guardar tu objeto"
                        .formatted(e.getId()))
                .build()
                .transform();
    }

    @ExceptionHandler(BusquedaSinResultadoException.class)
    public ResponseEntity<Response> handleBusquedaSinResultado(@NotNull BusquedaSinResultadoException e) {
        return ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .error("busqueda_sin_resultado")
                .message(e.getMessage())
                .hint("Escoge otro valor (%s) o atributo (%s) para realizar la busqueda"
                        .formatted(e.getValue(), e.getParam()))
                .build()
                .transform();
    }
}
