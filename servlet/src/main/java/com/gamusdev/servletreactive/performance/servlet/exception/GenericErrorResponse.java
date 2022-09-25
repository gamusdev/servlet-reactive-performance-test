package com.gamusdev.servletreactive.performance.servlet.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * GenericErrorResponse
 * Contains the information for a generic error
 */
@Getter
@Builder
public class GenericErrorResponse {
    /** HttpStatus */
    private HttpStatus status;
    /** message */
    private String message;
    /** error */
    private String error;
    /** timestamp */
    private LocalDateTime timestamp;
}
