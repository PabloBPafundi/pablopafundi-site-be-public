package com.pablopafundi.site.common.exception;

import com.pablopafundi.site.common.utils.ErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    /* Manejador de validaciones de jakarta */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(
                        HttpStatus.BAD_REQUEST.value(),
                        false,
                        errorMessage,
                        "Validation Error",
                        LocalDateTime.now()
                ));
    }


    @ExceptionHandler(IOException.class)
    public ResponseEntity<ErrorResponse> handleIOException(IOException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        false,
                        "There was an error processing the file.",
                        "FILE_PROCESSING_ERROR",
                        LocalDateTime.now()
                ));
    }



    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<ErrorResponse> handleMultipartException(MultipartException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        false,
                        "There was an error processing the file. Please check the file format and size.",
                        "FILE_PROCESSING_ERROR",
                        LocalDateTime.now()
                ));
    }

/* Manejador para validaciones de enum*/
@ExceptionHandler(HttpMessageNotReadableException.class)
public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex){
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    false,
                    "There was an error processing JSON. Please check check values of your keys",
                    "NOT_VALID_VALUE",
                    LocalDateTime.now()


            ));
}




    /* Manejador específico para violaciones de restricciones de datos */

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        // Puedes acceder a la causa de la excepción para obtener más detalles del error
        String errorMessage = "Violation of unique constraint or database error.";

        return ResponseEntity.status(HttpStatus.CONFLICT) // Código HTTP 409 para conflicto
                .body(new ErrorResponse(
                        HttpStatus.CONFLICT.value(),
                        false,
                        errorMessage,
                        "Conflict - Duplicate entry or constraint violation", // Mensaje específico
                        LocalDateTime.now()
                ));
    }


    /* Manegador para registros no encontrados */

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlerEntityNotFoundException(EntityNotFoundException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(
                        HttpStatus.NOT_FOUND.value(),
                        false,
                        ex.getMessage(),
                        "REGISTRY_NOT_FOUND",
                        LocalDateTime.now()


                ));

    }




    /* Manejador de todas las excepciones no manejadas */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralExceptions(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        //ex.getMessage(),
                        false,
                        "Contact Administrator",
                        "Internal Server Error",
                        LocalDateTime.now()
                ));
    }


    /* Manejador Personalizado de logicas de negocio: Ejemplo nombre unicos */
    @ExceptionHandler(BusinessLogicException.class)
    public ResponseEntity<ErrorResponse> handleBusinessLogicExceptions(BusinessLogicException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT) // Código de respuesta apropiado
                .body(new ErrorResponse(
                        HttpStatus.CONFLICT.value(),
                        false,
                        ex.getMessage(),
                        "BUSINESS_LOGIC_CONFLICT",
                        LocalDateTime.now()
                ));
    }


    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatusException(ResponseStatusException ex) {

        HttpStatusCode statusCode = ex.getStatusCode(); // Este es el tipo retornado por getStatusCode()

        // Convertir HttpStatusCode a HttpStatus
        HttpStatus status = HttpStatus.resolve(statusCode.value());

        // Verifica si el código de estado es 404 NOT_FOUND
        if (status == HttpStatus.NOT_FOUND) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(
                            HttpStatus.NOT_FOUND.value(),
                            false,
                            ex.getReason(),
                            "Resource Not Found",  // Mensaje personalizado de error
                            LocalDateTime.now()
                    ));
        }


        return null;
    }



    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED) // Código de respuesta apropiado
                .body(new ErrorResponse(
                        HttpStatus.UNAUTHORIZED.value(),
                        false,
                        ex.getMessage(),
                        "AUTHENTICATION_FAILURE",
                        LocalDateTime.now()
                ));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND) // Código de respuesta apropiado
                .body(new ErrorResponse(
                        HttpStatus.NOT_FOUND.value(),
                        false,
                        ex.getMessage(),
                        "USER_NOT_FOUND",
                        LocalDateTime.now()
                ));
    }



}


