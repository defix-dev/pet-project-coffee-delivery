package ru.defix.coffeedelivery.productRequest.service.dto;

import jakarta.annotation.Nullable;

import java.math.BigDecimal;

public record ProductUpdateRequestCreateParams(int productId, int submitterId, @Nullable String name, @Nullable BigDecimal price) {
}
