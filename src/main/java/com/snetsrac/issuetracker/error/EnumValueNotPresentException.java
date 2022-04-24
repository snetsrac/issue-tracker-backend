package com.snetsrac.issuetracker.error;

public class EnumValueNotPresentException extends RuntimeException {


    public EnumValueNotPresentException() {
    }

    public EnumValueNotPresentException(String message) {
        super(message);
    }

    public EnumValueNotPresentException(Throwable cause) {
        super(cause);
    }

    public EnumValueNotPresentException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public EnumValueNotPresentException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
}
