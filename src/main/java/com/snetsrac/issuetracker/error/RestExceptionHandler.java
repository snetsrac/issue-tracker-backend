package com.snetsrac.issuetracker.error;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class RestExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    private Logger log = LoggerFactory.getLogger(getClass());

    // Issue Tracker Exceptions

    @ExceptionHandler(BadRequestException.class)
    protected ResponseEntity<Object> handleBadRequestException(BadRequestException ex,
            HttpServletRequest request, HttpServletResponse response) {
                
        return handleExceptionInternal(HttpStatus.BAD_REQUEST, ex.getMessage());

    }

    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<Object> handleNotFoundException(NotFoundException ex,
            HttpServletRequest request, HttpServletResponse response) {

        return handleExceptionInternal(HttpStatus.NOT_FOUND, ex.getMessage());

    }

    @ExceptionHandler(InternalServerException.class)
    protected ResponseEntity<Object> handleInternalServerException(InternalServerException ex,
            HttpServletRequest request, HttpServletResponse response) {

        return handleExceptionInternal(HttpStatus.INTERNAL_SERVER_ERROR, "http.status.internal-server-error");

    }

    // Spring MVC Exceptions

    @ExceptionHandler(BindException.class)
    protected ResponseEntity<Object> handleBind(BindException ex,
            HttpServletRequest request, HttpServletResponse response) {

        return handleExceptionInternal(HttpStatus.BAD_REQUEST, "http.status.bad-request");

    }

    @ExceptionHandler(ConversionNotSupportedException.class)
    protected ResponseEntity<Object> handleConversionNotSupported(ConversionNotSupportedException ex,
            HttpServletRequest request, HttpServletResponse response) {

        log.error(ex.getMessage(), ex);
        return handleExceptionInternal(HttpStatus.INTERNAL_SERVER_ERROR, "http.status.internal-server-error");

    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleException(Exception ex,
            HttpServletRequest request, HttpServletResponse response) {

        log.error(ex.getMessage(), ex);
        return handleExceptionInternal(HttpStatus.INTERNAL_SERVER_ERROR, "http.status.internal-server-error");

    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex,
            HttpServletRequest request, HttpServletResponse response) {

        return handleExceptionInternal(HttpStatus.NOT_ACCEPTABLE, "http.status.not-acceptable");

    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
            HttpServletRequest request, HttpServletResponse response) {

        List<MediaType> mediaTypes = ex.getSupportedMediaTypes();

        if (!CollectionUtils.isEmpty(mediaTypes)) {
            if (HttpMethod.POST.toString() .equals(request.getMethod())) {
                response.setHeader("Accept-Post", StringUtils.collectionToCommaDelimitedString(mediaTypes));
            } else if (HttpMethod.PUT.toString().equals(request.getMethod())) {
                response.setHeader("Accept-Put", StringUtils.collectionToCommaDelimitedString(mediaTypes));
            }
        }

        return handleExceptionInternal(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "http.status.unsupported-media-type");

    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
            HttpServletRequest request, HttpServletResponse response) {

        return handleExceptionInternal(HttpStatus.BAD_REQUEST, "http.status.bad-request");

    }

    @ExceptionHandler(HttpMessageNotWritableException.class)
    protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex,
            HttpServletRequest request, HttpServletResponse response) {

        log.error(ex.getMessage(), ex);
        response.reset();
        return handleExceptionInternal(HttpStatus.INTERNAL_SERVER_ERROR, "http.status.internal-server-error");

    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
            HttpServletRequest request, HttpServletResponse response) {

        Set<HttpMethod> supportedMethods = ex.getSupportedHttpMethods();

        if (!CollectionUtils.isEmpty(supportedMethods)) {
            response.setHeader(HttpHeaders.ALLOW, StringUtils.collectionToCommaDelimitedString(supportedMethods));
        }

        return handleExceptionInternal(HttpStatus.METHOD_NOT_ALLOWED, "http.status.method-not-allowed");

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpServletRequest request, HttpServletResponse response) {

        Map<String, List<String>> errors = new HashMap<>();

        ex.getFieldErrors().forEach(error -> {
            errors.putIfAbsent(error.getField(), new ArrayList<>());
            errors.get(error.getField()).add(error.getDefaultMessage());
        });

        return handleExceptionInternal(HttpStatus.BAD_REQUEST, "http.status.bad-validation", errors);

    }

    @ExceptionHandler(MissingPathVariableException.class)
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex,
            HttpServletRequest request, HttpServletResponse response) {

        log.error(ex.getMessage(), ex);
        return handleExceptionInternal(HttpStatus.INTERNAL_SERVER_ERROR, "http.status.internal-server-error");

    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
            HttpServletRequest request, HttpServletResponse response) {

        return handleExceptionInternal(HttpStatus.BAD_REQUEST, "http.status.bad-request");

    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex,
            HttpServletRequest request, HttpServletResponse response) {

        return handleExceptionInternal(HttpStatus.BAD_REQUEST, "http.status.bad-request");

    }

    @ExceptionHandler(NoHandlerFoundException.class)
    protected ResponseEntity<Object> handleNoHandlerFound(NoHandlerFoundException ex,
            HttpServletRequest request, HttpServletResponse response) {

        log.warn("Page not found: " + request.getRequestURI());
        return handleExceptionInternal(HttpStatus.NOT_FOUND, "http.status.not-found");

    }

    @ExceptionHandler(ServletRequestBindingException.class)
    protected ResponseEntity<Object> handleServletRequestBinding(ServletRequestBindingException ex,
            HttpServletRequest request, HttpServletResponse response) {

        log.error(ex.getMessage(), ex);
        return handleExceptionInternal(HttpStatus.INTERNAL_SERVER_ERROR, "http.status.internal-server-error");

    }

    @ExceptionHandler(TypeMismatchException.class)
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex,
            HttpServletRequest request, HttpServletResponse response) {

        return handleExceptionInternal(HttpStatus.BAD_REQUEST, "http.status.bad-request");

    }

    // Spring Security Exception

    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<Object> handleAccessDenied(AccessDeniedException ex,
            HttpServletRequest request, HttpServletResponse response) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        log.warn("User " + auth.getName() + " attempted to access the protected resource: " +
                request.getMethod() + " " + request.getRequestURI());

        return handleExceptionInternal(HttpStatus.FORBIDDEN, "http.status.forbidden");

    }

    // Helper methods

    private ResponseEntity<Object> handleExceptionInternal(HttpStatus status,
            String messageId) {

        String message = messageSource.getMessage(messageId, null, LocaleContextHolder.getLocale());
        Problem problem = new Problem(status, message);
        return ResponseEntity.status(status).body(problem);

    }

    private ResponseEntity<Object> handleExceptionInternal(HttpStatus status,
            String messageId,
            Map<String, List<String>> errors) {

        String message = messageSource.getMessage(messageId, null, LocaleContextHolder.getLocale());
        Problem problem = new Problem(status, message, errors);
        return ResponseEntity.status(status).body(problem);

    }

}
