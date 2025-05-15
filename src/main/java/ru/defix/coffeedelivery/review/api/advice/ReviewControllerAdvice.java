package ru.defix.coffeedelivery.review.api.advice;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.defix.coffeedelivery.common.util.AdviceUtility;
import ru.defix.coffeedelivery.common.util.dto.FormattedError;
import ru.defix.coffeedelivery.review.exception.ReviewAlreadyExistsException;
import ru.defix.coffeedelivery.review.exception.ReviewNotFoundException;

@ControllerAdvice
public class ReviewControllerAdvice {
    @ExceptionHandler(ReviewAlreadyExistsException.class)
    public ResponseEntity<FormattedError> handleReviewAlreadyExistsException(Exception e, HttpServletRequest req) {
        return AdviceUtility.createErrorResponse(e, HttpStatus.CONFLICT, req);
    }

    @ExceptionHandler(ReviewNotFoundException.class)
    public ResponseEntity<FormattedError> handleReviewNotFoundException(Exception e, HttpServletRequest req) {
        return AdviceUtility.createErrorResponse(e, HttpStatus.NOT_FOUND, req);
    }
}
