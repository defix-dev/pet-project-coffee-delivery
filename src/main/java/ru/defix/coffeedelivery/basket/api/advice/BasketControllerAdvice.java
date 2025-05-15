package ru.defix.coffeedelivery.basket.api.advice;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.defix.coffeedelivery.basket.exception.BasketNotFoundException;
import ru.defix.coffeedelivery.common.util.AdviceUtility;
import ru.defix.coffeedelivery.common.util.dto.FormattedError;
import ru.defix.coffeedelivery.product.exception.ProductNotFoundException;

@ControllerAdvice
public class BasketControllerAdvice {
    @ExceptionHandler(BasketNotFoundException.class)
    public ResponseEntity<FormattedError> handleBasketNotFoundException(Exception e, HttpServletRequest req) {
        return AdviceUtility.createErrorResponse(e, HttpStatus.NOT_FOUND, req);
    }
}
