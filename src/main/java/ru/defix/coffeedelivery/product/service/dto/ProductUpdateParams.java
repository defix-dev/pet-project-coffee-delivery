package ru.defix.coffeedelivery.product.service.dto;

import jakarta.annotation.Nullable;

import java.math.BigDecimal;

public record ProductUpdateParams(int productId, @Nullable String name, @Nullable BigDecimal price) {
}
