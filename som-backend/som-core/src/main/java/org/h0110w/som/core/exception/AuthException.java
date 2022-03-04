package org.h0110w.som.core.exception;


public class AuthException extends RuntimeException {
    public AuthException(String message) {
        super(message);
    }

    public AuthException(String messasge, Throwable throwable) {
        super(messasge, throwable);
    }
}
