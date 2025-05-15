package ru.defix.coffeedelivery.common.api.advice;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import ru.defix.coffeedelivery.common.util.AdviceUtility;
import ru.defix.coffeedelivery.common.util.dto.FormattedError;

import java.nio.file.AccessDeniedException;

@ControllerAdvice
public class GlobalControllerAdvice {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<FormattedError> handleException(Exception e, HttpServletRequest req) {
        return AdviceUtility.createErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR, req);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<FormattedError> handleAccessDeniedException(Exception e, HttpServletRequest req) {
        return AdviceUtility.createErrorResponse(e, HttpStatus.FORBIDDEN, req);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<FormattedError> handleNoHandlerFoundException(Exception e, HttpServletRequest req) {
        return AdviceUtility.createErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR, req);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<FormattedError> handleMethodArgumentNotValidException(Exception e, HttpServletRequest req) {
        return AdviceUtility.createErrorResponse(e, HttpStatus.BAD_REQUEST, req);
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<FormattedError> handleMissingServletRequestPartException(Exception e, HttpServletRequest req) {
        return AdviceUtility.createErrorResponse(e, HttpStatus.BAD_REQUEST, req);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<FormattedError> handleMissingServletRequestParameterException(Exception e, HttpServletRequest req) {
        return AdviceUtility.createErrorResponse(e, HttpStatus.BAD_REQUEST, req);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<FormattedError> handleHttpMessageNotReadableException(Exception e, HttpServletRequest req) {
        return AdviceUtility.createErrorResponse(e, HttpStatus.BAD_REQUEST, req);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<FormattedError> handleIllegalArgumentException(Exception e, HttpServletRequest req) {
        return AdviceUtility.createErrorResponse(e, HttpStatus.BAD_REQUEST, req);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<FormattedError> handleAuthenticationException(Exception e, HttpServletRequest req) {
        return AdviceUtility.createErrorResponse(e, HttpStatus.UNAUTHORIZED, req);
    }
}
