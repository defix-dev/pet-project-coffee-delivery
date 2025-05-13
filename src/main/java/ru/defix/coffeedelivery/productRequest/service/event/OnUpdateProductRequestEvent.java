package ru.defix.coffeedelivery.productRequest.service.event;

import jakarta.annotation.Nullable;

import java.math.BigDecimal;

public record OnUpdateProductRequestEvent(int productId, @Nullable String name, @Nullable BigDecimal price) {
}
