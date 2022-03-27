package com.snetsrac.issuetracker.error;

import java.time.OffsetDateTime;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {

        String messages = ex.getFieldErrors()
                .stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining("\n"));

        Problem problem = new Problem(status, messages, OffsetDateTime.now());

        return ResponseEntity.status(status).body(problem);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {

        Problem problem = new Problem(status, "Required request body is missing.", OffsetDateTime.now());

        return ResponseEntity.status(status).body(problem);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {

        Problem problem = new Problem(status, ex.getMessage(), OffsetDateTime.now());

        return ResponseEntity.status(status).body(problem);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleExceptionCatchAll(Exception exc) {
        logger.error(exc.getMessage(), exc);

        Problem problem = new Problem(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred.", OffsetDateTime.now());

        return new ResponseEntity<>(problem, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
