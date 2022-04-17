package com.snetsrac.issuetracker.error;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import org.springframework.http.HttpStatus;

@JsonInclude(Include.NON_NULL)
public class Problem {
    private final HttpStatus httpStatus;
    private final String message;
    private final Map<String, List<String>> errors;
    private final OffsetDateTime timestamp;

    public Problem(HttpStatus status, String message) {
        this.httpStatus = status;
        this.message = message;
        this.errors = null;
        this.timestamp = OffsetDateTime.now();
    }

    public Problem(HttpStatus status, String message, Map<String, List<String>> errors) {
        this.httpStatus = status;
        this.message = message;
        this.errors = errors;
        this.timestamp = OffsetDateTime.now();
    }

    public int getStatus() {
        return httpStatus.value();
    }

    public String getTitle() {
        return httpStatus.getReasonPhrase();
    }

    public String getMessage() {
        return message;
    }

    public Map<String, List<String>> geterrors() {
        return errors;
    }

    public String getTimestamp() {
        return timestamp.truncatedTo(ChronoUnit.MILLIS).toString();
    }
}
