package org.h0110w.som.core.exception;

public class ServiceException extends RuntimeException {
    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String messasge, Throwable throwable) {
        super(messasge, throwable);
    }

    public ServiceException(Throwable throwable) {
        super(throwable.getMessage(), throwable);
    }
}
