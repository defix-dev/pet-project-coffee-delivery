package ru.defix.coffeedelivery.productRequest.exception;

public class ProductSellRequestNotFoundException extends RuntimeException {
    public ProductSellRequestNotFoundException() {
        super("Product sell request not found");
    }
}
