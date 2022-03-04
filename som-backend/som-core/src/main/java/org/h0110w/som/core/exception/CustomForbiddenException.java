package org.h0110w.som.core.exception;

public class CustomForbiddenException extends RuntimeException{
    public CustomForbiddenException(String message) {
        super(message);
    }

    public CustomForbiddenException(String messasge, Throwable throwable) {
        super(messasge, throwable);
    }

    public CustomForbiddenException(Throwable throwable) {
        super(throwable.getMessage(), throwable);
    }
}
