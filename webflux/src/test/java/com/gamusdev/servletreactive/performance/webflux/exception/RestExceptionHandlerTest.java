package com.gamusdev.servletreactive.performance.webflux.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.gamusdev.servletreactive.performance.webflux.exception.ExceptionConstants.GENERAL_ERROR;


/**
 * RestExceptionHandlerTest
 * Unitary testing for RestExceptionHandler
 */
@ExtendWith(SpringExtension.class)
public class RestExceptionHandlerTest {

    @InjectMocks
    private RestExceptionHandler handler;

    /**
     * Test GeneralException
     */
    @Test
    public void generalException() {
        // When
        final String message = "message";

        // Then
        ResponseEntity<GenericErrorResponse> resp = handler.generalException( new RuntimeException(message) );

        // Verify
        Assertions.assertEquals( HttpStatus.BAD_REQUEST, resp.getStatusCode() );
        Assertions.assertNotNull( resp.getBody() );
        Assertions.assertEquals( HttpStatus.INTERNAL_SERVER_ERROR, resp.getBody().getStatus() );
        Assertions.assertEquals( GENERAL_ERROR, resp.getBody().getMessage() );
        Assertions.assertEquals( message, resp.getBody().getError() );
        Assertions.assertNotNull(resp.getBody().getTimestamp());
    }
}
