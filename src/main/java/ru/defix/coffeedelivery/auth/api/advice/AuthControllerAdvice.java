package ru.defix.coffeedelivery.auth.api.advice;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.defix.coffeedelivery.auth.exception.InvalidJwtTokenException;
import ru.defix.coffeedelivery.basket.exception.BasketNotFoundException;
import ru.defix.coffeedelivery.common.util.AdviceUtility;
import ru.defix.coffeedelivery.common.util.dto.FormattedError;

@ControllerAdvice
public class AuthControllerAdvice {
    @ExceptionHandler(InvalidJwtTokenException.class)
    public ResponseEntity<FormattedError> handleInvalidJwtTokenException(Exception e, HttpServletRequest req) {
        return AdviceUtility.createErrorResponse(e, HttpStatus.FORBIDDEN, req);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<FormattedError> handleUsernameNotFoundException(Exception e, HttpServletRequest req) {
        return AdviceUtility.createErrorResponse(e, HttpStatus.NOT_FOUND, req);
    }
}
