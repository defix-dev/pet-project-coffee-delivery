package ru.defix.coffeedelivery.basket.api.dto.response;

import java.math.BigDecimal;

public record ProductPersonalData(
        String name,
        BigDecimal price,
        int productId,
        int ownerId
) { }
