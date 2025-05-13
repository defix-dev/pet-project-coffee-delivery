package ru.defix.coffeedelivery.productRequest.api.dto.response;

import java.math.BigDecimal;

public record ProductSellRequestData(int requestId, int submitterId, String name, BigDecimal price) {
}
