package ru.defix.coffeedelivery.productRequest.api.dto.response;

import java.math.BigDecimal;

public record ProductUpdateRequestData(int requestId, int submitterId, int productId, String name, BigDecimal price) {
}