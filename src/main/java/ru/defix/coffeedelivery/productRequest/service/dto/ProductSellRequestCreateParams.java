package ru.defix.coffeedelivery.productRequest.service.dto;

import java.math.BigDecimal;

public record ProductSellRequestCreateParams(int submitterId, String name, BigDecimal price) {
}
