package org.h0110w.som.core.exception;

public class InternalServiceException extends RuntimeException {
    public InternalServiceException(String message) {
        super(message);
    }

    public InternalServiceException(String messasge, Throwable throwable) {
        super(messasge, throwable);
    }

    public InternalServiceException(Throwable throwable) {
        super(throwable.getMessage(), throwable);
    }
}
