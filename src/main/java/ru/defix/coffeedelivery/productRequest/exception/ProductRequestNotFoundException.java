package ru.defix.coffeedelivery.productRequest.exception;

public class ProductRequestNotFoundException extends RuntimeException {
    public ProductRequestNotFoundException() {
        super("Product request not found");
    }
}
