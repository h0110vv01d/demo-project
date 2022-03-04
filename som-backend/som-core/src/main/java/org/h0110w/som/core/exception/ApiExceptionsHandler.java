package org.h0110w.som.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Main goal of this class is to handle any exception that being thrown and
 * return response with appropriate status code and message instead of stacktrace
 */
@ControllerAdvice()
public class ApiExceptionsHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public ErrorResponse onBadRequest(ServiceException exception) {
        return new ErrorResponse(400, exception.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ErrorResponse onDefaultException(Exception e) {
        return new ErrorResponse(500, e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(InternalServiceException.class)
    @ResponseBody
    public ErrorResponse onInternalException(InternalServiceException e) {
        return new ErrorResponse(500, e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ErrorResponse onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return new ErrorResponse(400, MethodArgumentNotValidExceptionProcessor.processMethodArgumentNotValidException(e));
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthException.class)
    @ResponseBody
    public ErrorResponse onAuthException(AuthException e) {
        return new ErrorResponse(401, e.getMessage());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(CustomForbiddenException.class)
    @ResponseBody
    public ErrorResponse onAccessDeniedExceptionException(CustomForbiddenException e) {
        return new ErrorResponse(403, e.getMessage());
    }
}
