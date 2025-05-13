package ru.defix.coffeedelivery.productRequest.exception;

public class ProductUpdateRequestNotFoundException extends RuntimeException {
    public ProductUpdateRequestNotFoundException() {
        super("Product update request not found");
    }
}
