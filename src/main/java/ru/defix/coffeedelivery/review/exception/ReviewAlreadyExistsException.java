package ru.defix.coffeedelivery.review.exception;

public class ReviewAlreadyExistsException extends RuntimeException {
    public ReviewAlreadyExistsException() {
        super("Review already exists");
    }
}
