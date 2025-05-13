package ru.defix.coffeedelivery.productRequest.exception;

public class ProductRequestAlreadyExistsException extends RuntimeException {
    public ProductRequestAlreadyExistsException() {
        super("Product request already exists");
    }
}
