package com.gamusdev.servletreactive.performance.servlet.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

import static com.gamusdev.servletreactive.performance.servlet.exception.ExceptionConstants.GENERAL_ERROR;


/**
 * RestExceptionHandler
 * Handler for the Rest exceptions
 */
@RestControllerAdvice
@Slf4j
public class RestExceptionHandler {

    /**
     * Parses the exception and creates the Rest response
     * @param ex exception
     * @return ResponseEntity with Generic Error Reponse
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<GenericErrorResponse> generalException(RuntimeException ex) {
        log.info("Handling exception:", ex);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(GenericErrorResponse.builder()
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .message(GENERAL_ERROR)
                        .error(ex.getMessage())
                        .timestamp(LocalDateTime.now())
                        .build()
                );
    }

}