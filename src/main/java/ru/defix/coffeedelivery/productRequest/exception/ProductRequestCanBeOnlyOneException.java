package ru.defix.coffeedelivery.productRequest.exception;

public class ProductRequestCanBeOnlyOneException extends RuntimeException {
    public ProductRequestCanBeOnlyOneException() {
        super("Product request can be only one");
    }
}
