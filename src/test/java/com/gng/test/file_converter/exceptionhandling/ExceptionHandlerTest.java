package com.gng.test.file_converter.exceptionhandling;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.gng.test.file_converter.model.ErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ExceptionHandlerTest {

    private ExceptionHandler exceptionHandler = new ExceptionHandler();

    @Test
    void shouldHandleException() {
        ResponseEntity<ErrorResponse> responseEntity = exceptionHandler.handleException(new Exception());

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        ErrorResponse errorResponse = responseEntity.getBody();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorResponse.getHttpErrorCode());
    }
}
