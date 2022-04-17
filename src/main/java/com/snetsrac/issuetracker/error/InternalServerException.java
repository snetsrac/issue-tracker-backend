package com.snetsrac.issuetracker.error;

public class InternalServerException extends RuntimeException {
    public InternalServerException() {
    }

    public InternalServerException(String message) {
        super(message);
    }

    public InternalServerException(Throwable cause) {
        super(cause);
    }

    public InternalServerException(String message, Throwable cause) {
        super(message, cause);
    }

    public InternalServerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
