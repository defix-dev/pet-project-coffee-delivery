package ru.defix.coffeedelivery.productRequest.service.event;

import ru.defix.coffeedelivery.db.entity.ProductRequest;

public record OnApproveProductRequestEvent(ProductRequest.Type requestType, int requestId) {
}
