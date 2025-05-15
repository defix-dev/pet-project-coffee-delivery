package ru.defix.coffeedelivery.review.api.dto.request;

import jakarta.validation.constraints.NotNull;

public record ReviewCreateData(@NotNull Integer productId, @NotNull String text) {
}
