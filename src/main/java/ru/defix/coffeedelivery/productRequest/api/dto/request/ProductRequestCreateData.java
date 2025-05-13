package ru.defix.coffeedelivery.productRequest.api.dto.request;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductRequestCreateData(@NotNull String name, @NotNull BigDecimal price) {
}
