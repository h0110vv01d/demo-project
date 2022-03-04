package org.h0110w.som.core.exception;

import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.StringJoiner;

/**
 * This class used to format MethodArgumentNotValidException message
 * this exception is being thrown when dtos fields with validation annotations contain invalid data
 * @see ApiExceptionsHandler
 */
public class MethodArgumentNotValidExceptionProcessor {
    public static String processMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        StringJoiner message = new StringJoiner(";");
        exception.getBindingResult().getAllErrors()
                .forEach(e ->
                        message.add(e.getDefaultMessage()));
        return message.toString();
    }
}
