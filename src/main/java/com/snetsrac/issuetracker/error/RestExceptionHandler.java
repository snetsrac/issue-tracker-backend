package com.snetsrac.issuetracker.error;

import java.net.URI;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
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

        URI instance = URI.create(((ServletWebRequest) request).getRequest().getRequestURI());

        Problem problem = Problem.create()
                .withTitle(status.getReasonPhrase())
                .withStatus(status)
                .withDetail(messages)
                .withInstance(instance);

        return ResponseEntity.status(status).body(problem);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        
        URI instance = URI.create(((ServletWebRequest) request).getRequest().getRequestURI());

        Problem problem = Problem.create()
                .withTitle(status.getReasonPhrase())
                .withStatus(status)
                .withDetail("Required request body is missing.")
                .withInstance(instance);

        return ResponseEntity.status(status).body(problem);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        URI instance = URI.create(((ServletWebRequest) request).getRequest().getRequestURI());

        Problem problem = Problem.create()
                .withTitle(status.getReasonPhrase())
                .withStatus(status)
                .withDetail(ex.getMessage())
                .withInstance(instance);

        return ResponseEntity.status(status).body(problem);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleExceptionCatchAll(Exception exc) {
        logger.error(exc.getMessage(), exc);

        Problem problem = Problem.create()
                .withTitle("Internal Server Error")
                .withStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .withDetail("An unexpected error occurred.");

        return new ResponseEntity<>(problem, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
