package com.snetsrac.issuetracker.error;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.http.HttpStatus;

public class Problem {
    private HttpStatus httpStatus;
    private String message;
    private OffsetDateTime timestamp;

    public Problem() {
    }

    public Problem(HttpStatus status, String message) {
        this.httpStatus = status;
        this.message = message;
        this.timestamp = OffsetDateTime.now();
    }

    public Problem(HttpStatus status, String message, OffsetDateTime timestamp) {
        this.httpStatus = status;
        this.message = message;
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return httpStatus.value();
    }

    public String getTitle() {
        return httpStatus.getReasonPhrase();
    }

    public void setHttpStatus(HttpStatus status) {
        this.httpStatus = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp.truncatedTo(ChronoUnit.MILLIS).toString();
    }

    public void setTimestamp(OffsetDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
