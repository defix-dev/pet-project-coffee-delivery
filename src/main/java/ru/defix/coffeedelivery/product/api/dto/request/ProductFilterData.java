package ru.defix.coffeedelivery.product.api.dto.request;

import jakarta.annotation.Nullable;

import java.math.BigDecimal;

public record ProductFilterData(
    @Nullable String name,
    @Nullable BigDecimal minPrice,
    @Nullable BigDecimal maxPrice,
    @Nullable BigDecimal price,
    @Nullable String ownerName,
    @Nullable Integer ownerId
) { }
