package ru.defix.coffeedelivery.productRequest.api.dto.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductRequestUpdateData(@NotNull int productId, @Nullable String name, @Nullable BigDecimal price) {
}
