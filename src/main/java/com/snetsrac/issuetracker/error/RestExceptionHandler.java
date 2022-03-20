package com.snetsrac.issuetracker.error;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler {
    Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(NotFoundException exc) {
        ErrorResponse response = new ErrorResponse(HttpStatus.NOT_FOUND.value(), exc.getMessage(), Instant.now());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(BadRequestException exc) {
        ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), exc.getMessage(),
                Instant.now());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(Exception exc) {
        logger.error(exc.getMessage(), exc);
        ErrorResponse response = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred.",
                Instant.now());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
