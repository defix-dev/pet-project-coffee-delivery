package ru.defix.coffeedelivery.product.api.advice;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.defix.coffeedelivery.common.util.AdviceUtility;
import ru.defix.coffeedelivery.common.util.dto.FormattedError;
import ru.defix.coffeedelivery.product.exception.ProductAlreadyExistsException;
import ru.defix.coffeedelivery.product.exception.ProductNotFoundException;
import ru.defix.coffeedelivery.productRequest.exception.ProductUpdateRequestNotFoundException;

@ControllerAdvice
public class ProductControllerAdvice {
    @ExceptionHandler(ProductAlreadyExistsException.class)
    public ResponseEntity<FormattedError> handleProductAlreadyExistsException(Exception e, HttpServletRequest req) {
        return AdviceUtility.createErrorResponse(e, HttpStatus.CONFLICT, req);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<FormattedError> handleProductNotFoundException(Exception e, HttpServletRequest req) {
        return AdviceUtility.createErrorResponse(e, HttpStatus.NOT_FOUND, req);
    }
}
