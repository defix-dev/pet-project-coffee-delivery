package ru.defix.coffeedelivery.productRequest.api.advice;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.defix.coffeedelivery.common.util.AdviceUtility;
import ru.defix.coffeedelivery.common.util.dto.FormattedError;
import ru.defix.coffeedelivery.productRequest.exception.*;
import ru.defix.coffeedelivery.review.exception.ReviewNotFoundException;

@ControllerAdvice
public class ProductRequestControllerAdvice {
    @ExceptionHandler(ProductRequestAlreadyExistsException.class)
    public ResponseEntity<FormattedError> handleProductRequestAlreadyExistsException(Exception e, HttpServletRequest req) {
        return AdviceUtility.createErrorResponse(e, HttpStatus.CONFLICT, req);
    }

    @ExceptionHandler(ProductRequestCanBeOnlyOneException.class)
    public ResponseEntity<FormattedError> handleProductRequestCanBeOnlyOneException(Exception e, HttpServletRequest req) {
        return AdviceUtility.createErrorResponse(e, HttpStatus.CONFLICT, req);
    }

    @ExceptionHandler(ProductRequestNotFoundException.class)
    public ResponseEntity<FormattedError> handleProductRequestNotFoundException(Exception e, HttpServletRequest req) {
        return AdviceUtility.createErrorResponse(e, HttpStatus.NOT_FOUND, req);
    }

    @ExceptionHandler(ProductSellRequestNotFoundException.class)
    public ResponseEntity<FormattedError> handleProductSellRequestNotFoundException(Exception e, HttpServletRequest req) {
        return AdviceUtility.createErrorResponse(e, HttpStatus.NOT_FOUND, req);
    }

    @ExceptionHandler(ProductUpdateRequestNotFoundException.class)
    public ResponseEntity<FormattedError> handleProductUpdateRequestNotFoundException(Exception e, HttpServletRequest req) {
        return AdviceUtility.createErrorResponse(e, HttpStatus.NOT_FOUND, req);
    }
}
