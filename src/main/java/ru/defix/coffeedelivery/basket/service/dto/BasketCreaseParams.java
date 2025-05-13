package ru.defix.coffeedelivery.basket.service.dto;

import jakarta.annotation.Nullable;

public record BasketCreaseParams(
        int userId,
        int productId,
        @Nullable Integer quantity
) { }
