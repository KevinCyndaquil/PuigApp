package org.puig.api.controller;

import com.mongodb.MongoWriteException;
import lombok.NonNull;
import org.puig.api.controller.responses.Response;
import org.puig.api.controller.responses.ErrorResponse;
import org.puig.api.util.errors.*;
import org.puig.api.util.errors.AltaEmpleadoInterrumpidaException.Reasons;
import org.puig.api.util.PuigLogger;
import org.puig.api.util.persistence.SimpleInstance;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.mongodb.MongoTransactionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.security.SignatureException;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class PuigExceptionController {

    private final PuigLogger logger = new PuigLogger(PuigExceptionController.class);

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<Response> handleTokenException$Invalid(@NonNull SignatureException e) {
        logger.exception(e);

        return ErrorResponse.builder()
                .error(Errors.sin_autorizacion_error)
                .status(HttpStatus.UNAUTHORIZED)
                .message(e.getMessage())
                .hint("Intenta obtener primero un token valido")
                .build()
                .transform();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Response> handleIllegalArgument(@NonNull IllegalArgumentException e) {
        logger.exception(e);

        return ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .error(Errors.argumento_invalido_error)
                .message(e.getMessage())
                .hint("Intenta ponerte en contacto con un administrador para obtener más información del error")
                .build()
                .transform();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response> handleMethodArgumentoNotValid(@NonNull MethodArgumentNotValidException e) {
        logger.exception(e);

        String message = e.getFieldError() == null ?
                "not defined" :
                e.getFieldError().getDefaultMessage();

        return ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .error(Errors.atributo_invalido_error)
                .message(message)
                .hint("Ponte en contacto con el administrador y utiliza el formato correcto para el campo %s"
                        .formatted(e.getFieldError().getField()))
                .build()
                .transform();
    }

    @ExceptionHandler(VentaInvalidaException.class)
    public ResponseEntity<Response> handleCreacionVenta(@NonNull VentaInvalidaException e) {
        logger.exception(e);

        return ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .error(Errors.finalizacion_venta_error)
                .message(e.getMessage())
                .hint(e.getHint())
                .build()
                .transform();
    }

    @ExceptionHandler(MongoWriteException.class)
    public ResponseEntity<Response> handleMongoWrite(MongoWriteException e) {
        logger.exception(e);

        return ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .error(Errors.servicio_error)
                .message("Ocurrio un error interno del servidor")
                .hint("Llama a Dios")
                .build()
                .transform();
    }

    @ExceptionHandler(LlaveDuplicadaException.class)
    public ResponseEntity<Response> handleLlaveDuplicada(@NonNull LlaveDuplicadaException e) {
        logger.exception(e);

        return ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .error(Errors.llave_duplicada_error)
                .message(e.getMessage())
                .hint("Escoge otro identificador en lugar de %s para guardar tu objeto"
                        .formatted(e.showId()))
                .build()
                .transform();
    }

    @ExceptionHandler(BusquedaSinResultadoException.class)
    public ResponseEntity<Response> handleBusquedaSinResultado(@NonNull BusquedaSinResultadoException e) {
        logger.exception(e);

        return ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .error(Errors.busqueda_sin_resultado_error)
                .message(e.getMessage())
                .hint("Realiza una busqueda con diferentes parametros a %s=%s o ponte en contacto con el administrador"
                        .formatted(e.getParam(), e.getValue()))
                .build()
                .transform();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Response> handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
        logger.exception(e);

        return ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .error(Errors.mensaje_no_reconocible_error)
                .message("Cuerpo de la solicitud no pudo ser entendida por el servidor")
                .hint("Intenta verficar la sintaxis del mensaje o llama a un administrador para obtener más información")
                .build()
                .transform();
    }

    @ExceptionHandler(MongoTransactionException.class)
    public ResponseEntity<Response> handleMongoTransaction(@NonNull MongoTransactionException e) {
        logger.exception(e);

        return ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .error(Errors.rollback_transaction_error)
                .message(e.getMessage())
                .hint("Ponte en contacto con el administrador para poder entender a mejor ticket el error")
                .build()
                .transform();
    }

    @ExceptionHandler(AltaEmpleadoInterrumpidaException.class)
    public ResponseEntity<Response> handleAltaEmpleadoInterrumpida(@NonNull AltaEmpleadoInterrumpidaException e) {
        logger.exception(e);

        return ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .error(Errors.rollback_transaction_error)
                .message(e.getMessage())
                .hint(e.getReason() == Reasons.ID_ERROR ?
                        "Intenta guardar al empleado con otro nickname" :
                        "Busca ayuda de un administrador para resolver el problema")
                .build()
                .transform();
    }

    @ExceptionHandler({SimpleInstance.InstancedException.class})
    public ResponseEntity<Response> handleIllegalAccess(SimpleInstance.InstancedException e) {
        logger.exception(e);

        return ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .error(Errors.servidor_error)
                .message("Ocurrio un error durante uno de nuestros procedimientos")
                .hint("Busca ayuda de un administrador para resolver el problema")
                .build()
                .transform();
    }

    @ExceptionHandler(NombreUnicoRepetidoException.class)
    public ResponseEntity<Response> handleNombreUnicoRepetido(@NonNull NombreUnicoRepetidoException e) {
        logger.exception(e);

        return ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .error(Errors.atributo_invalido_error)
                .message(e.getMessage())
                .hint("Intenta utilizar otro nombre o usa el que ya está creado")
                .build()
                .transform();
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Response> handleAccessDenied(@NonNull AccessDeniedException e) {
        logger.exception(e);

        return ErrorResponse.builder()
                .status(HttpStatus.UNAUTHORIZED)
                .error(Errors.sin_autorizacion_error)
                .message("No se tienen los permisos necesarios para el recurso")
                .hint("Llama al administrador y pide una promoción de usuario")
                .build()
                .transform();
    }

    @ExceptionHandler(PasswordIncorrectoException.class)
    public ResponseEntity<Response> handlePasswordIncorrecto(@NonNull PasswordIncorrectoException e) {
        logger.exception(e);

        return ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .error(Errors.password_invalido_error)
                .message(e.getMessage())
                .hint("Intenta utilizar otra contraseña con el usuario")
                .build()
                .transform();
    }
}
